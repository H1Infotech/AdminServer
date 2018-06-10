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
public class SensorData implements Comparable<SensorData> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String beeBoxNo;
	private Long farmerId;
	private BigDecimal lat;
	private BigDecimal lng;
	private Date createDate;
	private Double gravity;
	private Double battery;	
	private Double humidity;
	private Double temperature;
	private Double airPressure;
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getBeeBoxNo() {
		return beeBoxNo;
	}
	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
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
	public Double getBattery() {
		return battery;
	}
	public void setBattery(Double battery) {
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

	@Override
	public int compareTo(SensorData o) {
		if(o==null || o.getBeeBoxNo()==null) {
			return 1;
		}
		if(this.getBeeBoxNo()==null) {
			return -1;
		}
		return beeBoxNo.compareTo(o.getBeeBoxNo());
	}
}
