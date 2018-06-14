package com.h1infotech.smarthive.domain;

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "historyAlertEvent")
public class HistoryAlertEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long beeBoxId;
	private Long adminId;
	private String event;
	private String handleWay;
	private Date createDate;

	@JsonIgnore
	public String getDesc() {
    	DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
		return id+"_"+event+"_"+handleWay+"_"+createDate==null?null:df3.format(createDate);
	}
	
	public Long getBeeBoxId() {
		return beeBoxId;
	}

	public void setBeeBoxId(Long beeBoxId) {
		this.beeBoxId = beeBoxId;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getHandleWay() {
		return handleWay;
	}

	public void setHandleWay(String handleWay) {
		this.handleWay = handleWay;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
