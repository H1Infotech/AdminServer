package com.h1infotech.smarthive.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.SensorDataRepository;

@Service
public class SensorDataServiceImpl implements SensorDataService {

	@Autowired
	BeeBoxService beeBoxService;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Override
	public SensorData getLatestSensorData(Long latestSensorData) {
		Optional<SensorData>  sensorData = sensorDataRepository.findById(latestSensorData);
		if(sensorData.isPresent()) {
			return sensorData.get();
		}
		return null;
	}
	
	public List<SensorData> getSensorDara(List<Long> ids) {
		if(ids==null || ids.size()==0) {
			return null;
		}
		return sensorDataRepository.findByIdIn(ids);
	}
	
}
