package com.h1infotech.smarthive.web.request;

public class BeeFarmerPageRetrievalRequest {
	private Integer pageSize;
	private Integer pageNo;
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
}
