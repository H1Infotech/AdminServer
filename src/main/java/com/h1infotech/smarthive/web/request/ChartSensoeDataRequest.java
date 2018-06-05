package com.h1infotech.smarthive.web.request;

import java.util.Date;

public class ChartSensoeDataRequest {
	
	private Date beginDate;
	private Date endDate;
	private Long boxId;
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getBoxId() {
		return boxId;
	}
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}
}
