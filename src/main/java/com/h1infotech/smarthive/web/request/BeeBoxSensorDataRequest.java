package com.h1infotech.smarthive.web.request;

import java.util.List;

public class BeeBoxSensorDataRequest {
	private List<String> beeBoxNos;

	public List<String> getBeeBoxNos() {
		return beeBoxNos;
	}

	public void setBeeBoxNos(List<String> beeBoxNos) {
		this.beeBoxNos = beeBoxNos;
	}
}
