package com.h1infotech.smarthive.service;

import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import java.util.HashMap;
import java.io.IOException;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import org.slf4j.LoggerFactory;
import javax.websocket.Session;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.websocket.server.PathParam;
import org.apache.commons.lang3.StringUtils;
import javax.websocket.server.ServerEndpoint;
import com.h1infotech.smarthive.domain.BeeBox;
import org.springframework.stereotype.Component;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.SensorData;
import org.springframework.beans.factory.annotation.Autowired;

@ServerEndpoint(value = "/sensorDataWebsocket/{userName}/{beeBoxId1}") 
@Component
public class BeeBoxWebSocketServer {

	private static final Logger logger = LoggerFactory.getLogger(BeeBoxWebSocketServer.class);
	private Session session;
	private String userName;
	private Long farmerId = 0L;
	private Long beeBoxId = 0L;
	private Long lastSendDataId = 0L;
	private static int onlineCount = 0;
	private static Map<String, BeeBoxWebSocketServer> webSocketMap = new HashMap<String, BeeBoxWebSocketServer>();

	@Autowired
	private BeeBoxService beeBoxService;
	
	@OnOpen  
    public void onOpen(@PathParam("userName") String userName, @PathParam("beeBoxId1") String beeBoxId1, Session session) throws Exception {
		onlineCount++;
		Long beeBoxId = Long.parseLong(beeBoxId1);
		logger.info("====Connecting the Bee Box WebSocket for User: {}, User Id: {} and Bee Box: {}, Total Online Connections: {}====", userName, farmerId, beeBoxId, onlineCount);
		BeeBox beeBox =beeBoxService.getBeeBoxByUserNameAndId(userName, beeBoxId);
		
		if(beeBox==null) {
			sendMessage(JSONObject.toJSONString(Response.fail("No Such Bee Box")));
			onClose();
			return;
		}
		this.session  = session;  
		this.userName = userName;
        this.lastSendDataId = 0L;
		this.beeBoxId = beeBox.getId();
		this.farmerId = beeBox.getFarmerId();
		String websocketMapKey = farmerId + "_" + beeBoxId;
		webSocketMap.put(websocketMapKey, this);
		sendMessage(JSONObject.toJSONString(Response.success("Connecting Success")));
    }  
	
	@OnClose  
    public void onClose() {  
		logger.info("====Disconnecting from Servcer for User: {}, User Id: {}, and Total Connections: {}====", userName, farmerId, onlineCount);
		webSocketMap.remove(userName); 
    }  
	
    @OnError  
    public void onError(Session session, Throwable error) {  
		logger.error("====WebSocket Error for User Name:{}, User Id: {}, beeBoxId: {}, and lastSendDataId: {}====",userName, farmerId, beeBoxId,lastSendDataId);
        error.printStackTrace();  
    }  
    
    public void sendMessage(SensorData sensorData) {  
    	logger.info("====Send Message: {} to User: {}====",JSONArray.toJSONString(sensorData), userName);
    	if(sensorData == null) {
    		return;
    	}
    	if(lastSendDataId < sensorData.getId()) {
    		this.lastSendDataId = sensorData.getId();
    	}
        try {
			this.session.getBasicRemote().sendText(JSONArray.toJSONString(sensorData));
		} catch (IOException e) {
			logger.warn("====Send Sensor Data Error:"+ JSONArray.toJSONString(sensorData) +"====");;
		}  
    } 
        
    public void sendMessage(String message) throws IOException {  
    	logger.info("====Send Message: {} to User: {}, User Id: {}====", userName, farmerId, message);
        this.session.getBasicRemote().sendText(message);  
    } 
    
    public static void receiveSensorDataMessage(String sensorDataStr) {
    	logger.info("====WebSoket Message Got from Publisher: {}====", sensorDataStr);
    	List<SensorData> sensorDataList = null;
    	try {
    		if(!StringUtils.isEmpty(sensorDataStr)) {
    			sensorDataList = JSONArray.parseArray(sensorDataStr, SensorData.class);
    		}
    	}catch(Exception e) {
    		logger.warn("====Parse Sensor Data Error:{}====",sensorDataStr);
    	}
    	
    	if(sensorDataList != null && sensorDataList.size() != 0) {
			for (SensorData sensorData : sensorDataList) {
				try {
					String websocketMapKey = sensorData.getFarmerId() + "_" + sensorData.getId();
					BeeBoxWebSocketServer beeBoxWebSocketServer = webSocketMap.get(websocketMapKey);
					beeBoxWebSocketServer.sendMessage(sensorData);
				} catch (Exception e) {
					logger.warn("====Send Message to User Id: {} and Message: {}====", sensorData.getFarmerId(), JSONObject.toJSON(sensorData));
				}
			}
		}

    }
  
}
