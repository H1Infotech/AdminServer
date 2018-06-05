package com.h1infotech.smarthive.web.request;

import java.util.List;

public class AdminDeletionRequest {
	private List<Long> adminIds;

	public List<Long> getAdminIds() {
		return adminIds;
	}
	public void setAdminIds(List<Long> adminIds) {
		this.adminIds = adminIds;
	}
}
