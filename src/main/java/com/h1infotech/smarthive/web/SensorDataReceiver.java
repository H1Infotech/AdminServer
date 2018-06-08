package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.service.SensorDataEvaluationService;
import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;

@RestController
@RequestMapping("/api")
public class SensorDataReceiver {

	private Logger logger = LoggerFactory.getLogger(SensorDataReceiver.class);

	@Autowired
	SensorDataEvaluationService sensorDataEvaluationService;
	
	@Autowired
	BeeBoxRepository beeBoxRepository;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
    @PostMapping(path = "/receiveSensorData")
    @ResponseBody
	public Response<String> receiveSensorData(HttpServletRequest httpRequest, @RequestBody RecevierSensorDataRequest request) {
		try {
			logger.info("====Catching the Sensor Data: {}====", JSONObject.toJSONString(request));
			if (request == null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			SensorData sensorData = sensorDataRepository.save(request.getSensorData());
			BeeBox beeBox = beeBoxRepository.findByFarmerId(request.getFarmerId());
			beeBox.setLatestSensorDataId(sensorData.getId());
			beeBox.setLat(sensorData.getLat());
			beeBox.setLng(sensorData.getLng());
			beeBoxRepository.save(beeBox);
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
