package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.SensorData;

public interface SensorDataService {
	SensorData getLatestSensorData(Long latestSensorData);
}
