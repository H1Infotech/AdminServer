package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "beeBox")
public class BeeBox {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long farmerId;
	private String batchNo;
	private Integer status;
	private BigDecimal lat;
	private BigDecimal lng;
	private Date entryDate;
	private String manufacturer;
	private Date productionDate;
	private Long latestSensorDataId;
	private Boolean protectionStrategy;
	private Date updateSensorDataTime;
	
	public Date getUpdateSensorDataTime() {
		return updateSensorDataTime;
	}
	public void setUpdateSensorDataTime(Date updateSensorDataTime) {
		this.updateSensorDataTime = updateSensorDataTime;
	}
	public Boolean getProtectionStrategy() {
		return protectionStrategy;
	}
	public void setProtectionStrategy(Boolean protectionStrategy) {
		this.protectionStrategy = protectionStrategy;
	}
	public Long getLatestSensorDataId() {
		return latestSensorDataId;
	}
	public void setLatestSensorDataId(Long latestSensorDataId) {
		this.latestSensorDataId = latestSensorDataId;
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
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	} 
	@Override
	public String toString() {
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); 
		return id+"_"+farmerId+"_"+batchNo+"_"+lat+"_"+lng+"_"+mediumDateFormat.format(entryDate)+"_"+manufacturer+"_"+mediumDateFormat.format(productionDate);
	}
}
