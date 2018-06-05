package com.h1infotech.smarthive.web.response;

import java.util.List;
import com.h1infotech.smarthive.domain.Admin;

public class AdminPageRetrievalResponse {
	private Integer currentPageNo;
	private Integer totalPageNo;
	private List<Admin> admins;
	
	public List<Admin> getAdmins() {
		return admins;
	}
	public void setAdmins(List<Admin> admins) {
		this.admins = admins;
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
