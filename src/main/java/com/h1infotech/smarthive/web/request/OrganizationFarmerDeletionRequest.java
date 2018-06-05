package com.h1infotech.smarthive.web.request;

public class OrganizationFarmerDeletionRequest {
	private Long farmerId;
	private Long organizationId;
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}
	
}
