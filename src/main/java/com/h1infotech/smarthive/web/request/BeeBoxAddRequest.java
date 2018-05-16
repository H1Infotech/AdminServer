package com.h1infotech.smarthive.web.request;

import java.math.BigDecimal;

public class BeeBoxAddRequest {

	private Integer status;
	private BigDecimal lat;
	private BigDecimal lng;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
