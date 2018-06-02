package com.h1infotech.smarthive.job;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import java.text.DateFormat;
import org.slf4j.LoggerFactory;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.domain.IntervalSensorData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.Configuration;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.h1infotech.smarthive.repository.IntervalSensorDataRepository;

@Configuration
@EnableScheduling
public class StatisticalSensorDataJob {

	private Logger logger = LoggerFactory.getLogger(StatisticalSensorDataJob.class);
	
	public static final Integer INTERVAL_TIME = 600000;
	
	@Autowired
	BeeBoxRepository beeBoxRepository;

	@Autowired
	BeeFarmerRepository beeFarmerRepository;

	@Autowired
	SensorDataRepository sensorDataRepository;

	@Autowired
	IntervalSensorDataRepository intervalSensorDataRepository;

	@Scheduled(cron = "0 0/10 * * * ?")
	private void configureTasks() {
		try {
			DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
			Date now = new Date();
			Date startTime = new Date(now.getTime() - INTERVAL_TIME);
			logger.info("====Start Job to Calculate the Sensor Data(Start:{} --- End:{})====", mediumDateFormat.format(startTime),mediumDateFormat.format(now));
			List<BeeFarmer> beeFarmers = beeFarmerRepository.findAll();
			if (beeFarmers == null || beeFarmers.size() == 0) {
				logger.info("====Calculate the Sensor Data End(Every 10 Mins)====");
				return;
			}
			List<BeeBox> beeBoxes = null;
			List<SensorData> sensorData = null;
			for (BeeFarmer beeFarmer : beeFarmers) {
				beeBoxes = beeBoxRepository.findBeeBoxesByFarmerId(beeFarmer.getId());
				if (beeBoxes == null || beeBoxes.size() == 0) {
					continue;
				}
				for (BeeBox beeBox : beeBoxes) {
					double temperature = 0.0;
					double humidity = 0.0;
					double airPressure = 0.0;
					double gravity = 0.0;
					double battery = 0.0;
					if(beeBox.getFarmerId()==null) {
						continue;
					}
					sensorData = sensorDataRepository.findByFarmerIdIsAndCreateDateBetween(beeBox.getFarmerId(), startTime, now);
					if (sensorData == null || sensorData.size() == 0) {
						continue;
					}
					for (SensorData data : sensorData) {
						temperature += data.getTemperature() == null ? 0.0 : data.getTemperature();
						humidity += data.getHumidity() == null ? 0.0 : data.getHumidity();
						airPressure += data.getAirPressure() == null ? 0.0 : data.getAirPressure();
						gravity += data.getGravity() == null ? 0.0 : data.getGravity();
						battery += data.getBattery() == null ? 0.0 : data.getBattery();
					}
					temperature /= sensorData.size();
					humidity /= sensorData.size();
					airPressure /= sensorData.size();
					gravity /= sensorData.size();
					battery /= sensorData.size();
					IntervalSensorData intervalSensorData = new IntervalSensorData();
					intervalSensorData.setTemperature(temperature);
					intervalSensorData.setHumidity(humidity);
					intervalSensorData.setAirPressure(airPressure);
					intervalSensorData.setGravity(gravity);
					intervalSensorData.setBattery(battery);
					intervalSensorData.setFarmerId(beeFarmer.getId());
					intervalSensorData.setBoxId(beeBox.getId());
					intervalSensorData.setCreateDate(new Date());
					intervalSensorData.setStartTime(startTime);
					intervalSensorData.setEndTime(now);
					intervalSensorDataRepository.save(intervalSensorData);
				}
			}
			logger.info("====Calculate the Sensor Data End(Every 10 Mins)====");
		} catch (Exception e) {
			logger.error("====Job Exection Error====", e);
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * ?")
	private void detectBeeBoxOffLine() {
		logger.info("====Detect Bee Box OffLine Job Start(Every 5 Mins)====");
		List<BeeBox> beeBoxes = beeBoxRepository.findAll();
		if(beeBoxes==null || beeBoxes.size()==0) {
			logger.info("====Detect Bee Box OffLine Job End(Every 5 Mins)====");
			return;
		}
		Date now = new Date();
		for(BeeBox beeBox: beeBoxes) {
			if(beeBox.getStatus()==BeeBoxStatusEnum.OFFLINE_STATUS.getStatus()) {
				continue;
			}
			if(beeBox.getUpdateSensorDataTime()==null 
					||now.getTime()-beeBox.getUpdateSensorDataTime().getTime()>5*60*1000) {
				beeBox.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
				beeBox.setLatestSensorDataId(null);
				beeBoxRepository.save(beeBox);
			}
		}
		logger.info("====Detect Bee Box OffLine Job End(Every 5 Mins)====");
	}
	
}
