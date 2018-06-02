package com.h1infotech.smarthive.web.request;

import java.util.Date;

public class ChartSensoeDataRequest {
	
	private Date beginDate;
	private Date endData;
	private Long boxId;
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndData() {
		return endData;
	}
	public void setEndData(Date endData) {
		this.endData = endData;
	}
	public Long getBoxId() {
		return boxId;
	}
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}
}
