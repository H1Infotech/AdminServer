package com.h1infotech.smarthive.web.request;

public class PasswordUpteRequest {
	
	private String password;
	private String firstTime;  //"1" means the first to change the password
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}
}
