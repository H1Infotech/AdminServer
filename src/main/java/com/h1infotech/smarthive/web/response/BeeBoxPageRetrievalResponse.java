package com.h1infotech.smarthive.web.response;

import java.util.List;

import com.h1infotech.smarthive.domain.BeeBox;

public class BeeBoxPageRetrievalResponse {
	private Integer currentPageNo;
	private Integer totalPageNo;
	private List<BeeBox> beeBoxes;
	
	public List<BeeBox> getBeeBoxes() {
		return beeBoxes;
	}
	public void setBeeBoxes(List<BeeBox> beeBoxes) {
		this.beeBoxes = beeBoxes;
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
