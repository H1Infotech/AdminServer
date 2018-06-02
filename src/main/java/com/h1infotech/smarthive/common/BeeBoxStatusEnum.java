package com.h1infotech.smarthive.common;

public enum BeeBoxStatusEnum {
	RUNNING_STATUS(0),
	NORMAL_STATUS(1),
	ABNORMAL_STATUS(2),
	OFFLINE_STATUS(3),
	;	
	private int status;
	private BeeBoxStatusEnum(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
}
