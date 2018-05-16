package com.h1infotech.smarthive.web.request;

import java.util.List;

public class UserDeleteBeeBoxRequest {
	private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
