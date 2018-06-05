package com.h1infotech.smarthive.web.request;

import java.util.List;

public class BeeBoxSensorDataRequest {
	private List<Long> beeBoxIds;

	public List<Long> getBeeBoxIds() {
		return beeBoxIds;
	}

	public void setBeeBoxIds(List<Long> beeBoxIds) {
		this.beeBoxIds = beeBoxIds;
	}
}
