package com.h1infotech.smarthive.web.response;

import java.util.List;
import com.h1infotech.smarthive.domain.SensorData;

public class SensorDateRetrievalResponse {
	private Integer totalBeeBoxNum=0;
	private Integer normalBeeBoxNum=0;
	private Integer abnormalBeeBoxNum=0;
	private Integer runningBeeBoxNum=0;
	private Integer offLineBeeBoxNum=0;
	private Integer protectionNum=0;
	private Integer noProtectionNum=0;
	private List<SensorData> latestSensorData;
	
	public Integer getNoProtectionNum() {
		return noProtectionNum;
	}
	public void setNoProtectionNum(Integer noProtectionNum) {
		this.noProtectionNum = noProtectionNum;
	}
	public Integer getTotalBeeBoxNum() {
		return totalBeeBoxNum;
	}
	public void setTotalBeeBoxNum(Integer totalBeeBoxNum) {
		this.totalBeeBoxNum = totalBeeBoxNum;
	}
	public Integer getNormalBeeBoxNum() {
		return normalBeeBoxNum;
	}
	public void setNormalBeeBoxNum(Integer normalBeeBoxNum) {
		this.normalBeeBoxNum = normalBeeBoxNum;
	}
	public Integer getAbnormalBeeBoxNum() {
		return abnormalBeeBoxNum;
	}
	public void setAbnormalBeeBoxNum(Integer abnormalBeeBoxNum) {
		this.abnormalBeeBoxNum = abnormalBeeBoxNum;
	}
	public Integer getRunningBeeBoxNum() {
		return runningBeeBoxNum;
	}
	public void setRunningBeeBoxNum(Integer runningBeeBoxNum) {
		this.runningBeeBoxNum = runningBeeBoxNum;
	}
	public Integer getOffLineBeeBoxNum() {
		return offLineBeeBoxNum;
	}
	public void setOffLineBeeBoxNum(Integer offLineBeeBoxNum) {
		this.offLineBeeBoxNum = offLineBeeBoxNum;
	}
	public Integer getProtectionNum() {
		return protectionNum;
	}
	public void setProtectionNum(Integer protectionNum) {
		this.protectionNum = protectionNum;
	}
	public List<SensorData> getLatestSensorData() {
		return latestSensorData;
	}
	public void setLatestSensorData(List<SensorData> latestSensorData) {
		this.latestSensorData = latestSensorData;
	}
}
