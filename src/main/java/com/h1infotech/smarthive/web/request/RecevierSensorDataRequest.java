package com.h1infotech.smarthive.web.request;

import java.util.Date;
import java.math.BigDecimal;
import com.h1infotech.smarthive.domain.SensorData;

public class RecevierSensorDataRequest {
	private String beeBoxNo;
	private Long farmerId;
	private BigDecimal lat;
	private BigDecimal lng;
	private Double gravity;
	private Double battery;
	private Double humidity;
	private Double temperature;
	private Double airPressure;

	public SensorData getSensorData() {
		SensorData sensorData = new SensorData();
		sensorData.setBeeBoxNo(beeBoxNo);
		sensorData.setFarmerId(farmerId);
		sensorData.setLat(lat);
		sensorData.setLng(lng);
		sensorData.setCreateDate(new Date());
		sensorData.setGravity(gravity);
		sensorData.setBattery(battery);
		sensorData.setHumidity(humidity);
		sensorData.setTemperature(temperature);
		sensorData.setAirPressure(airPressure);
		sensorData.setStatus(1);
		return sensorData;
	}

	public String getBeeBoxNo() {
		return beeBoxNo;
	}

	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}

	public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public Double getGravity() {
		return gravity;
	}

	public void setGravity(Double gravity) {
		this.gravity = gravity;
	}

	public Double getBattery() {
		return battery;
	}

	public void setBattery(Double battery) {
		this.battery = battery;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(Double airPressure) {
		this.airPressure = airPressure;
	}
}
