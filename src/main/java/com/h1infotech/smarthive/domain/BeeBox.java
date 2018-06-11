package com.h1infotech.smarthive.domain;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.GregorianCalendar;
import javax.persistence.criteria.Root;
import com.alibaba.fastjson.JSONObject;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.h1infotech.smarthive.web.request.FilterItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;

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
	
	public String getBeeBoxNo() {
		return beeBoxNo;
	}

	public void setBeeBoxNo(String beeBoxNo) {
		this.beeBoxNo = beeBoxNo;
	}

	@JsonIgnore
	public String getDesc() {
    	DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
    	String desc = id+"_"+farmerId+"_"+batchNo+"_"+beeBoxNo
    			 +"_"+lat+"_"+lng+"_"+manufacturer
    			 +"_"+latestSensorDataId+"_";
    	if(entryDate!=null) {
    		desc += df3.format(entryDate)+"_";
    	}
    	if(productionDate!=null) {
    		desc += df3.format(productionDate)+"_";
    	}
    	if(updateSensorDataTime!=null) {
    		desc += df3.format(updateSensorDataTime)+"_";
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
	
	@Autowired
	private static BeeFarmerRepository beeFarmerRepository;
	
	public static Specification<BeeBox> getCondition(FilterItem filterItem) {
		return new Specification<BeeBox>() {
			private static final long serialVersionUID = -4421852559886689923L;
			@Override
			public Predicate toPredicate(Root<BeeBox> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				System.err.println("====Filter: " + JSONObject.toJSONString(filterItem) + "====");

				List<Predicate> predicates = new ArrayList<>();
				if(filterItem.getBeeFarmerIds()!=null && filterItem.getBeeFarmerIds().size()>0) {
					Expression<Long> exp = root.<Long>get("farmerId");
					predicates.add(exp.in(filterItem.getBeeFarmerIds()));
				}
				if (!StringUtils.isEmpty(filterItem.getBatchNo())) {
					predicates.add(cb.equal(root.<String>get("beeBoxNo"), filterItem.getBatchNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getMaxBatchNo())) {
					predicates.add(cb.lessThan(root.<String>get("beeBoxNo"), filterItem.getMaxBatchNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getMinBatchNo())) {
					predicates.add(cb.greaterThan(root.<String>get("beeBoxNo"), filterItem.getMinBatchNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getBeeFarmerName())) {
					List<BeeFarmer> beeFarmers = beeFarmerRepository.findByNameLike("%" + filterItem.getBeeFarmerName() + "%");
					List<Long> ids = new LinkedList<Long>();
					if (beeFarmers != null && beeFarmers.size() == 0) {
						predicates.add(root.<Long>get("farmerId").in(ids));
					} else {
						return null;
					}
				}
				if (filterItem.getBeeFarmerId() != null) {
					Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(filterItem.getBeeFarmerId());
					if (beeFarmer == null) {
						return null;
					} else {
						predicates.add(cb.equal(root.<Long>get("farmerId"), beeFarmer.get().getId()));
					}
				}
				if (!StringUtils.isEmpty(filterItem.getBatchNo())) {
					predicates.add(cb.like(root.<String>get("batchNo"), "%" + filterItem.getBatchNo() + "%"));
				}
				if (!StringUtils.isEmpty(filterItem.getMinBatchNo())) {
					predicates.add(cb.greaterThan(root.<String>get("batchNo"), filterItem.getMinBatchNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getMaxBatchNo())) {
					predicates.add(cb.lessThan(root.<String>get("batchNo"), filterItem.getMaxBatchNo()));
				}
				if (filterItem.getProductionDate() != null) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(filterItem.getProductionDate());
					calendar.add(Calendar.DATE, 1);
					Date maxDate = calendar.getTime();
					predicates.add(cb.between(root.<Date>get("productionDate"), filterItem.getProductionDate(), maxDate));
				}
				if(!StringUtils.isEmpty(filterItem.getManfaucturer())) {
					predicates.add(cb.like(root.<String>get("manufacturer"), "%" + filterItem.getManfaucturer() + "%"));
				}
				if (filterItem.getMinLatitude() != null) {
					predicates.add(cb.greaterThan(root.<BigDecimal>get("lat"), filterItem.getMinLatitude()));
				}
				if (filterItem.getMaxLatitude() != null) {
					predicates.add(cb.lessThan(root.<BigDecimal>get("lat"), filterItem.getMaxLatitude()));
				}
				if (filterItem.getMinLongitude() != null) {
					predicates.add(cb.greaterThan(root.<BigDecimal>get("lng"), filterItem.getMinLongitude()));
				}
				if (filterItem.getMaxLongitude() != null) {
					predicates.add(cb.lessThan(root.<BigDecimal>get("lng"), filterItem.getMaxLongitude()));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
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
