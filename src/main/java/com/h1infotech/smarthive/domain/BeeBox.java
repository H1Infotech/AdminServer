package com.h1infotech.smarthive.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSONObject;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.web.request.FilterItem;

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
				if (filterItem.getId() != null) {
					predicates.add(cb.equal(root.<Long>get("id"), filterItem.getId()));
				}
				if (filterItem.getMaxId() != null) {
					predicates.add(cb.lessThan(root.<Long>get("id"), filterItem.getMaxId()));
				}
				if (filterItem.getMinId() != null) {
					predicates.add(cb.greaterThan(root.<Long>get("id"), filterItem.getMinId()));
				}
				if (!StringUtils.isEmpty(filterItem.getBeeFarmerName())) {
					List<BeeFarmer> beeFarmers = beeFarmerRepository
							.findByNameLike("%" + filterItem.getBeeFarmerName() + "%");
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
	@Override
	public String toString() {
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); 
		return id+"_"+farmerId+"_"+batchNo+"_"+lat+"_"+lng+"_"+mediumDateFormat.format(entryDate)+"_"+manufacturer+"_"+mediumDateFormat.format(productionDate);
	}
}
