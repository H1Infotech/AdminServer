package com.h1infotech.smarthive.common;

public enum BizCodeEnum {

	SERVICE_SUCCESS("000000","Service Success"),
	LOGIN_ERROR("000010","Login Error"),
	REGISTER_ERROR("000020","User Register Error"),
	REGISTER_USER_EXIST_ERROR("000021","User Already Exists"),
	NO_USER_INFO("000030","No User Information"),
	DATABASE_ACCESS_ERROR("000040","Database Access Error"),
	ILLEGAL_INPUT("000090","Illegal Input Parameter(s)"),
	SERVICE_ERROR("999999","Service Error"),
	
	
	;
	
	private String code;
	private String message;
	
	private BizCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
}
