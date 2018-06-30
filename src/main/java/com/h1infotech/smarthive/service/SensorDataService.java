package com.h1infotech.smarthive.service;

import java.util.List;

import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;

public interface SensorDataService {
	SensorData getLatestSensorData(Long latestSensorData);
	List<SensorData> getSensorDara(List<Long> ids);
	void insertSensorData(RecevierSensorDataRequest request);
}
