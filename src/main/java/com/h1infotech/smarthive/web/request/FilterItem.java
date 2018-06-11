package com.h1infotech.smarthive.web.request;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class FilterItem {
	private String minBeeBoxNo;
	private String maxBeeBoxNo;
	private String beeBoxno;
	private String beeFarmerName;
	private Long beeFarmerId;
	private String manfaucturer;
	private String batchNo;
	private String minBatchNo;
	private String maxBatchNo;
	private Date productionDate;
	private BigDecimal minLongitude;
	private BigDecimal maxLongitude;
	private BigDecimal minLatitude;
	private BigDecimal maxLatitude;
	private List<Long> beeFarmerIds;
	
	public List<Long> getBeeFarmerIds() {
		return beeFarmerIds;
	}

	public void setBeeFarmerIds(List<Long> beeFarmerIds) {
		this.beeFarmerIds = beeFarmerIds;
	}

	public String getMinBeeBoxNo() {
		return minBeeBoxNo;
	}

	public void setMinBeeBoxNo(String minBeeBoxNo) {
		this.minBeeBoxNo = minBeeBoxNo;
	}

	public String getMaxBeeBoxNo() {
		return maxBeeBoxNo;
	}

	public void setMaxBeeBoxNo(String maxBeeBoxNo) {
		this.maxBeeBoxNo = maxBeeBoxNo;
	}

	public String getBeeBoxno() {
		return beeBoxno;
	}

	public void setBeeBoxno(String beeBoxno) {
		this.beeBoxno = beeBoxno;
	}

	public String getBeeFarmerName() {
		return beeFarmerName;
	}

	public void setBeeFarmerName(String beeFarmerName) {
		this.beeFarmerName = beeFarmerName;
	}

	public Long getBeeFarmerId() {
		return beeFarmerId;
	}

	public void setBeeFarmerId(Long beeFarmerId) {
		this.beeFarmerId = beeFarmerId;
	}

	public String getManfaucturer() {
		return manfaucturer;
	}

	public void setManfaucturer(String manfaucturer) {
		this.manfaucturer = manfaucturer;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getMinBatchNo() {
		return minBatchNo;
	}

	public void setMinBatchNo(String minBatchNo) {
		this.minBatchNo = minBatchNo;
	}

	public String getMaxBatchNo() {
		return maxBatchNo;
	}

	public void setMaxBatchNo(String maxBatchNo) {
		this.maxBatchNo = maxBatchNo;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public BigDecimal getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(BigDecimal minLongitude) {
		this.minLongitude = minLongitude;
	}

	public BigDecimal getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(BigDecimal maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public BigDecimal getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(BigDecimal minLatitude) {
		this.minLatitude = minLatitude;
	}

	public BigDecimal getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(BigDecimal maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
}
