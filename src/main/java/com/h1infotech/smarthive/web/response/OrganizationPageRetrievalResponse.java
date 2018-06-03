package com.h1infotech.smarthive.web.response;

import java.util.List;
import com.h1infotech.smarthive.domain.Organization;

public class OrganizationPageRetrievalResponse {
	private Integer currentPageNo;
	private Integer totalPageNo;
	private List<Organization> organizations;
	
	public List<Organization> getOrganizations() {
		return organizations;
	}
	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}
	public Integer getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public Integer getTotalPageNo() {
		return totalPageNo;
	}
	public void setTotalPageNo(Integer totalPageNo) {
		this.totalPageNo = totalPageNo;
	}
}
