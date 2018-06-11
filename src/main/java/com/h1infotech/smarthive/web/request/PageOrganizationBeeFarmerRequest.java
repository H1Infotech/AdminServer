package com.h1infotech.smarthive.web.request;

import java.util.List;

public class PageOrganizationBeeFarmerRequest {
	private List<Long> organizationIds;
	private Integer pageNo;
	private Integer pageSize;
	public List<Long> getOrganizationIds() {
		return organizationIds;
	}
	public void setOrganizationIds(List<Long> organizationIds) {
		this.organizationIds = organizationIds;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
