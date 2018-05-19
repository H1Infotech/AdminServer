package com.h1infotech.smarthive.domain;

import java.util.Date;
import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "sensorData")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long boxId;
	private Long farmerId;
	private BigDecimal lat;
	private BigDecimal lng;
	private Date createDate;
	private BigDecimal gravity;
	private BigDecimal battery;	
	private BigDecimal humidity;
	private BigDecimal temperature;
	private BigDecimal airPressure;
	
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
	public BigDecimal getTemperature() {
		return temperature;
	}
	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}
	public BigDecimal getHumidity() {
		return humidity;
	}
	public void setHumidity(BigDecimal humidity) {
		this.humidity = humidity;
	}
	public BigDecimal getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(BigDecimal airPressure) {
		this.airPressure = airPressure;
	}
	public BigDecimal getGravity() {
		return gravity;
	}
	public void setGravity(BigDecimal gravity) {
		this.gravity = gravity;
	}
	public BigDecimal getBattery() {
		return battery;
	}
	public void setBattery(BigDecimal battery) {
		this.battery = battery;
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
}
