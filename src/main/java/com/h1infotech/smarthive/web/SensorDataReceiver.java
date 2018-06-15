package com.h1infotech.smarthive.web;

import java.util.Set;
import java.util.List;
import java.util.Date;
import org.slf4j.Logger;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import com.h1infotech.smarthive.domain.Event;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.SmsSender;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.domain.SmsSender.KV;
import com.h1infotech.smarthive.domain.HistoryAlertEvent;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.EventRepository;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.service.SensorDataEvaluationService;
import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;
import com.h1infotech.smarthive.repository.HistoryAlertEventRepository;
import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;

@RestController
@RequestMapping("/api")
public class SensorDataReceiver {

	private Logger logger = LoggerFactory.getLogger(SensorDataReceiver.class);
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	BeeBoxRepository beeBoxRepository;
	
	@Autowired
	SensorDataEvaluationService sensorDataEvaluationService;
	
	@Autowired
	BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	
	@Autowired
	HistoryAlertEventRepository historyAlertEventRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	SmsSender smsSender;
	
    @PostMapping(path = "/receiveSensorData")
    @ResponseBody
	public Response<String> receiveSensorData(HttpServletRequest httpRequest, @RequestBody RecevierSensorDataRequest request) {
		try {
			logger.info("====Catching the Sensor Data: {}====", JSONObject.toJSONString(request));
			if (request == null || StringUtils.isEmpty(request.getBeeBoxNo())) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			BeeBox beeBox = beeBoxRepository.findByBeeBoxNo(request.getBeeBoxNo());			
			if(beeBox==null) {
				throw new BusinessException(BizCodeEnum.NO_BEE_BOX_INFO);
			}
			
			SensorData sensorData = sensorDataRepository.save(request.getSensorData());
			
			beeBox.setLatestSensorDataId(sensorData.getId());
			beeBox.setLat(sensorData.getLat());
			beeBox.setLng(sensorData.getLng());
			beeBoxRepository.save(beeBox);
			
			List<Event> events=null;
			Set<Long> ids = new HashSet<Long>();
			List<BeeBoxGroupAssociation> associations = beeBoxGroupAssociationRepository.findByBeeBoxId(beeBox.getId());
			logger.info("====Associations: {}====",JSONArray.toJSONString(associations));
			if(associations!=null && associations.size()>0) {
				for(BeeBoxGroupAssociation one: associations) {
					ids.add(one.getGroupId());
				}
				List<Long> idList = new ArrayList<>(ids);
				events = eventRepository.findByGroupId(idList);
			}
			if(events==null || events.size()==0) {
				return Response.success(null);
			}
			for(Event event: events) {
				boolean normalStatus = sensorDataEvaluationService.evaluate(event.getAction(), sensorData);
				if(!normalStatus) {
					BeeBox beeBoxDB = beeBoxRepository.findByBeeBoxNo(sensorData.getBeeBoxNo());
					if(beeBoxDB!=null) {
						beeBoxDB.setStatus(BeeBoxStatusEnum.ABNORMAL_STATUS.getStatus());
						beeBoxRepository.save(beeBoxDB);
						Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(beeBoxDB.getFarmerId());
						if(beeFarmer.isPresent() && !StringUtils.isEmpty(beeFarmer.get().getMobile())) {
							KV[] kvs = new KV[4];
							kvs[0] = new KV("name", beeFarmer.get().getName());
							kvs[1] = new KV("boxNo", ""+beeBoxDB.getId());
							String dataType = null;
							String value = null;
							if(1 == event.getRuleType()) {
								dataType="温度";
								value=""+sensorData.getTemperature();
							}else if(2 == event.getRuleType()) {
								dataType="湿度";
								value=""+sensorData.getHumidity();
							}else if(3 == event.getRuleType()) {
								dataType="压强";
								value=""+sensorData.getAirPressure();
							}else if(4 == event.getRuleType()) {
								dataType="电量";
								value=""+sensorData.getBattery();
							}else if(5 == event.getRuleType()) {
								dataType="重力";
								value=""+sensorData.getGravity();
							}
							kvs[2] = new KV("dataType",dataType);
							kvs[3] = new KV("value", value);
							String desc = "蜂箱: "+beeBoxDB.getBeeBoxNo()+", "+dataType+"异常: "+value;
							HistoryAlertEvent newEvent = new HistoryAlertEvent();
							newEvent.setAdminId(event.getAdminId());
							newEvent.setCreateDate(new Date());
							newEvent.setBeeBoxId(beeBoxDB.getId());
							newEvent.setEvent(desc);
							newEvent.setHandleWay(null);
							historyAlertEventRepository.save(newEvent);
							smsSender.dispatchSMSService("2298876", beeFarmer.get().getMobile(), kvs);
						}
					}
				}
			}
			
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
    }
	
}
