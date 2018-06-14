package com.h1infotech.smarthive.web.request;

public class SensorDataTag {
	private String beeBoxNo;
	private Long lastestSensorDataId;
	
	public String getBeeBoxNo() {
		return beeBoxNo;
	}
	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}
	public Long getLastestSensorDataId() {
		return lastestSensorDataId;
	}
	public void setLastestSensorDataId(Long lastestSensorDataId) {
		this.lastestSensorDataId = lastestSensorDataId;
	}
}
