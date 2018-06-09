package com.h1infotech.smarthive.web.request;

public class SMSRequest {
	
	private String userName;
	private String messageType;
	private String mobile;
	private String registerFlag;
	
	public String getRegisterFlag() {
		return registerFlag;
	}
	public void setRegisterFlag(String registerFlag) {
		this.registerFlag = registerFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
