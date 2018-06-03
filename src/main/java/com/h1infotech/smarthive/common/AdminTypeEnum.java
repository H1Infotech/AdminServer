package com.h1infotech.smarthive.common;

public enum AdminTypeEnum {
	SUPER_ADMIN(1),
	SENIOR_ADMIN(2),
	ORGANIZATION_ADMIN(3),
	NO_ORGANIZATION_ADMIN(4),
	;
	private int type;
	
	private AdminTypeEnum(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public static AdminTypeEnum getEnum(int type) {
		if(type==1) {
			return SUPER_ADMIN;
		}else if(type==2) {
			return SENIOR_ADMIN;
		}else if(type==3) {
			return ORGANIZATION_ADMIN;
		}else if(type==4) {
			return NO_ORGANIZATION_ADMIN;
		}
		return null;
	}
}
