package com.h1infotech.smarthive.web.request;

import java.util.Date;

public class ChartSensoeDataRequest {
	
	private Date beginDate;
	private Date endDate;
	private String beeBoxNo;
	
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
	public String getBeeBoxNo() {
		return beeBoxNo;
	}
	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}
}
