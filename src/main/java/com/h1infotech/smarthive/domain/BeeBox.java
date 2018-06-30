package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "beeBox")
public class BeeBox  implements Comparable<BeeBox> {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String beeBoxNo;
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
	@Transient
	private Double battery;
	
	public Double getBattery() {
		return battery;
	}

	public void setBattery(Double battery) {
		this.battery = battery;
	}

	public String getBeeBoxNo() {
		return beeBoxNo;
	}

	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}

	@JsonIgnore
	public String getDesc() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	String desc = id+"_"+farmerId+"_"+batchNo+"_"+beeBoxNo
    			 +"_"+lat+"_"+lng+"_"+manufacturer
    			 +"_"+latestSensorDataId+"_";
    	
    	
    	if(entryDate!=null) {
    		desc += formatter.format(entryDate);
    	}
    	if(productionDate!=null) {
    		desc += formatter.format(productionDate)+"_";
    	}
    	if(updateSensorDataTime!=null) {
    		desc += formatter.format(updateSensorDataTime)+"_";
    	}
    	if(status ==null) {
    		status=0;
    	}
    	if(status==0) {
    		desc+="正常";
    	}else if(status==2) {
    		desc+="异常";
    	}else if(status==3) {
    		desc+="离线";
    	}
    	return desc;
	}
	
	@Override
	public int compareTo(BeeBox o) {
		if(o==null || o.getId()==null) {
			return 1;
		}
		if(this.id==null) {
			return -1;
		}
		long diff = this.id - o.getId();
		if(diff==0) {
			return 0;
		}else if(diff>0) {
			return 1;
		}else {
			return -1;
		}
	}
	

	

	
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
}
