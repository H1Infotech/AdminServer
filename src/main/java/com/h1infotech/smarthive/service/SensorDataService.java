package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.web.response.SensorDateRetrievalResponse;

public interface SensorDataService {
	SensorData getLatestSensorData(Long latestSensorData);
	SensorDateRetrievalResponse getAllLatestSensorData(Long farmerId);
}
