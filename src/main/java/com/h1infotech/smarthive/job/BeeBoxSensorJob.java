//package com.h1infotech.smarthive.job;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import com.h1infotech.smarthive.domain.SensorData;
//import com.h1infotech.smarthive.service.SensorDataService;
//import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;
//
//@Configuration
//@EnableScheduling
//public class BeeBoxSensorJob {
//	private Logger logger = LoggerFactory.getLogger(BeeBoxSensorJob.class);
//
//	@Autowired
//	SensorDataService sensorDataService;
//
//	private static BigDecimal lat = new BigDecimal(31.22);
//	private static BigDecimal lng = new BigDecimal(121.48);
//	private static Double gravity = 9.18;
//	private static Double battery = 80.1;
//	private static Double humidity = 10.1;
//	private static Double temperature = 30.1;
//	private static Double airPressure = 1000.1;
//
//	@Scheduled(fixedRate = 5000)
//	private void sendSensorData() {
//		try {
//			logger.info("====Send Sensor Data(Every 5 Sends)====");
//
//			SensorData sensorData1 = new SensorData();
//			SensorData sensorData2 = new SensorData();
//			sensorData1.setBeeBoxNo("000000001");
//			sensorData2.setBeeBoxNo("000000002");
//			sensorData1.setFarmerId(1L);
//			sensorData2.setFarmerId(1L);
//			sensorData1.setLat(lat.add(new BigDecimal(0.11)));
//			sensorData2.setLat(lat);
//			sensorData1.setLng(lng.add(new BigDecimal(0.11)));
//			sensorData2.setLng(lng);
//			sensorData1.setCreateDate(new Date());
//			sensorData2.setCreateDate(new Date());
//			if (gravity < 12.0) {
//				gravity += 0.1;
//			} else {
//				gravity = 9.18;
//			}
//			sensorData1.setGravity(gravity);
//			sensorData2.setGravity(gravity);
//			if (battery < 0.5) {
//				battery -= 0.1;
//			} else {
//				battery = 80.1;
//			}
//			sensorData1.setBattery(battery);
//			sensorData2.setBattery(battery);
//			if (humidity < 15) {
//				humidity += 0.1;
//			} else {
//				humidity = 10.1;
//			}
//			sensorData1.setHumidity(humidity);
//			sensorData2.setHumidity(humidity);
//			if (temperature < 36) {
//				temperature += 0.1;
//			} else {
//				temperature = 30.1;
//			}
//			RecevierSensorDataRequest request = new RecevierSensorDataRequest();
//			request.setAirPressure(airPressure);
//			request.setBattery(sensorData1.getBattery());
//			request.setBeeBoxNo("000000001");
//			request.setFarmerId(1L);
//			request.setGravity(sensorData1.getGravity());
//			request.setHumidity(sensorData1.getHumidity());
//			request.setLat(sensorData1.getLat());
//			request.setLng(sensorData1.getLng());
//			request.setTemperature(sensorData1.getTemperature());
//			sensorDataService.insertSensorData(request);
//			request.setAirPressure(airPressure);
//			request.setBattery(sensorData2.getBattery());
//			request.setBeeBoxNo("000000002");
//			request.setFarmerId(1L);
//			request.setGravity(sensorData2.getGravity());
//			request.setHumidity(sensorData2.getHumidity());
//			request.setLat(sensorData2.getLat());
//			request.setLng(sensorData2.getLng());
//			request.setTemperature(sensorData2.getTemperature());
//			sensorDataService.insertSensorData(request);
//		} catch (Exception e) {
//			logger.error("====Insert Sensor Data Error: ", e);
//		}
//	}
//}
