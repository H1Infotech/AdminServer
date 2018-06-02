package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "intervalSensorData")
public class IntervalSensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long farmerId;
	private Long boxId;
	private Date createDate;
	private Double temperature;
	private Double humidity;
	private Double airPressure;
	private Double gravity;
	private Double battery;
	private Date startTime;
	private Date endTime;

	
	public Double getBattery() {
		return battery;
	}

	public void setBattery(Double battery) {
		this.battery = battery;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(Double airPressure) {
		this.airPressure = airPressure;
	}

	public Double getGravity() {
		return gravity;
	}

	public void setGravity(Double gravity) {
		this.gravity = gravity;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
