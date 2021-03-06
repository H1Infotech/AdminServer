package com.h1infotech.smarthive.web.request;

import java.util.Date;
import java.math.BigDecimal;

import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.domain.BeeBox;

public class BeeBoxAddRequest {

	private Long id;
	private Long farmerId;
	private String beeBoxNo;
	private String manufacturer;
	private Date productionDate;
	private String batchNo;
	private Integer status;
	private BigDecimal lat;
	private BigDecimal lng;
	private Long latestSensorDataId;
	private Date updateSensorDataTime;
	private Date entryDate;
	private Boolean protectionStrategy;
	private String code;
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BeeBox getBeeBoxAdd() {
		BeeBox beeBox = new BeeBox();
		beeBox.setFarmerId(farmerId);
		beeBox.setBeeBoxNo(beeBoxNo);
		beeBox.setManufacturer(manufacturer);
		beeBox.setProductionDate(productionDate);
		beeBox.setBatchNo(batchNo);
		beeBox.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
		beeBox.setEntryDate(new Date());
		beeBox.setProtectionStrategy(false);
		return beeBox;
	}
	
	public BeeBox getBeeBoxUpdate() {
		BeeBox beeBox = new BeeBox();
		beeBox.setId(id);
		beeBox.setBeeBoxNo(beeBoxNo);
		beeBox.setFarmerId(farmerId);
		beeBox.setManufacturer(manufacturer);
		beeBox.setProductionDate(productionDate);
		beeBox.setBatchNo(batchNo);
		beeBox.setStatus(status);
		beeBox.setEntryDate(entryDate);
		beeBox.setProtectionStrategy(protectionStrategy);
		return beeBox;
	}
	
	public String getBeeBoxNo() {
		return beeBoxNo;
	}

	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public Long getLatestSensorDataId() {
		return latestSensorDataId;
	}

	public void setLatestSensorDataId(Long latestSensorDataId) {
		this.latestSensorDataId = latestSensorDataId;
	}

	public Date getUpdateSensorDataTime() {
		return updateSensorDataTime;
	}

	public void setUpdateSensorDataTime(Date updateSensorDataTime) {
		this.updateSensorDataTime = updateSensorDataTime;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Boolean getProtectionStrategy() {
		return protectionStrategy;
	}

	public void setProtectionStrategy(Boolean protectionStrategy) {
		this.protectionStrategy = protectionStrategy;
	}
}
