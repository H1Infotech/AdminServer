package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.service.SensorDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;

@RestController
@RequestMapping("/api")
public class SensorDataReceiver {

	private Logger logger = LoggerFactory.getLogger(SensorDataReceiver.class);

	@Autowired
	SensorDataService sensorDataService;
	
    @PostMapping(path = "/receiveSensorData")
    @ResponseBody
	public Response<String> receiveSensorData(HttpServletRequest httpRequest, @RequestBody RecevierSensorDataRequest request) {
		try {
			sensorDataService.insertSensorData(request);
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
