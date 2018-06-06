package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "beeBoxGroup")
public class BeeBoxGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String groupName;
	private Long adminId;
	private Integer beeBoxNum;
	private Date createDate;
	
	public Integer getBeeBoxNum() {
		return beeBoxNum;
	}

	public void setBeeBoxNum(Integer beeBoxNum) {
		this.beeBoxNum = beeBoxNum;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
