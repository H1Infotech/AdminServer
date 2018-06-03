package com.h1infotech.smarthive.common;

public enum AdminRightEnum {
	ADMIN_MANAGEMENT(1),
	ORGANIZATION_MANAGEMENT(2),
	BEEFARMER_MANAGEMENT(3),
	BEEBOX_MANAGEMENT(4),
	EVENT_MANAGEMENT(5),
	;
	
	private int right;
	
	private AdminRightEnum(int right) {
		this.right = right;
	}
	
	public int getRight() {
		return right;
	}
	
	public AdminRightEnum getRight(int right) {
		if(right==1) {
			return ADMIN_MANAGEMENT;
		}else if(right==2) {
			return ORGANIZATION_MANAGEMENT;
		}else if(right==3) {
			return BEEFARMER_MANAGEMENT;
		}else if(right==4) {
			return BEEBOX_MANAGEMENT;
		}else if(right==5) {
			return EVENT_MANAGEMENT;
		}
		return null;
	}
}
