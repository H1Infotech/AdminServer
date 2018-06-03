package com.h1infotech.smarthive.web.response;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeFarmer;

public class BeeFarmerPageRetrievalResponse {
	private Integer currentPageNo;
	private Integer totalPageNo;
	private List<BeeFarmer> beeFarmers;
	
	public List<BeeFarmer> getBeeFarmers() {
		return beeFarmers;
	}
	public void setBeeFarmers(List<BeeFarmer> beeFarmers) {
		this.beeFarmers = beeFarmers;
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
