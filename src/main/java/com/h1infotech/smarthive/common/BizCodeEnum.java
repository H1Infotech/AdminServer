package com.h1infotech.smarthive.common;

public enum BizCodeEnum {

	SERVICE_SUCCESS("000000","Service Success"),
	LOGIN_ERROR("000010","Login Error"),
	REGISTER_ERROR("000020","User Register Error"),
	REGISTER_USER_EXIST_ERROR("000021","User Already Exists"),
	NO_USER_INFO("000030","No User Information"),
	NO_FARMER_INFO("000031","No Farmer Informarion"),
	NO_RIGHT("000031","No Right"),
	USER_NAME_INEXISTENCE("000031","User Name Does Not Exist"),
	USER_NO_MOBILE_NUM_IN_DB("000032","No User Mobile Record"),
	MOBILE_NUM_INCONSISTENT("000033","Inconsistent Mobile Number"),
	WRONG_SMS_CODE("000034","WRONG SMS Code"),
	NO_BEE_BOX_INFO("000035","No Bee Box Info"),
	DATABASE_ACCESS_ERROR("000040","Database Access Error"),
	ADD_EMPTY_BEEBOX("000050","Add an Empty BeeBox"),
	NO_SUCH_EVENT("000060","No such event"),
	ILLEGAL_INPUT("000090","Illegal Input Parameter(s)"),
	BEE_BOX_GROUP_EXISTS("000100","Group Name Already Exists"),
	BEE_BOX_NUMBER_EXISTS("000110","BEE BOX NO EXISTS"),
	EXPIRATION_TOKEN("000120","Token Expiration"),
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
