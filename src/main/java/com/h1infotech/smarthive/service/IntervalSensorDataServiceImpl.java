package com.h1infotech.smarthive.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.domain.IntervalSensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h1infotech.smarthive.repository.IntervalSensorDataRepository;

@Service
public class IntervalSensorDataServiceImpl implements IntervalSensorDataService {
	
	@Autowired
	IntervalSensorDataRepository intervalSensorDataRepository;
	
	@Override
	public List<IntervalSensorData> getIntervalSensorData(Long boxId, Date beginDate, Date endData) {
		try {
			List<IntervalSensorData> sensorData = intervalSensorDataRepository.findByBoxIdIsAndCreateDateBetweenOrderByCreateDateAsc(boxId, beginDate, endData);
			if(sensorData.size() <= 300) {
				return sensorData;
			}
			int batchSize = (int) Math.ceil(sensorData.size()/300.0);
			int count = 0;
			double temperature = 0.0;
			double humidity = 0.0;
			double airPressure = 0.0;
			double gravity = 0.0;
			double battery = 0.0;
			List<IntervalSensorData> mergedSensorData = new LinkedList<IntervalSensorData>();
			for(int i = 0; i < sensorData.size(); i++) {
				temperature += sensorData.get(i).getTemperature()==null?0.0:sensorData.get(i).getTemperature();
				humidity += sensorData.get(i).getHumidity()==null?0.0:sensorData.get(i).getHumidity();
				airPressure += sensorData.get(i).getAirPressure()==null?0.0:sensorData.get(i).getAirPressure();
				gravity += sensorData.get(i).getGravity()==null?0.0:sensorData.get(i).getGravity();
				battery += sensorData.get(i).getBattery()==null?0.0:sensorData.get(i).getBattery();
				if(count++ == batchSize-1) {
					temperature /= batchSize;
					humidity /= batchSize;
					airPressure /= batchSize;
					gravity /= batchSize;
					battery /= batchSize;
					IntervalSensorData intervalSensorData = new IntervalSensorData();
					intervalSensorData.setBoxId(boxId);
					intervalSensorData.setBattery(battery);
					intervalSensorData.setGravity(gravity);
					intervalSensorData.setHumidity(humidity);
					intervalSensorData.setAirPressure(airPressure);
					intervalSensorData.setTemperature(temperature);
					intervalSensorData.setId(sensorData.get(i).getId());
					intervalSensorData.setStartTime(sensorData.get(i).getStartTime());
					mergedSensorData.add(intervalSensorData);
					count=0;
					temperature = 0.0;
					humidity = 0.0;
					airPressure = 0.0;
					battery = 0.0;
				}
			}
			return mergedSensorData;
		}catch(BusinessException e) {
			
		}catch(Exception e) {
			
		}
		return null;
	}

}
