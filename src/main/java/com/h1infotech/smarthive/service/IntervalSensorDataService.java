package com.h1infotech.smarthive.service;

import java.util.Date;
import java.util.List;
import com.h1infotech.smarthive.domain.IntervalSensorData;

public interface IntervalSensorDataService {
	List<IntervalSensorData> getIntervalSensorData(String beeBoxNo, Date beginDate, Date endData);
}
