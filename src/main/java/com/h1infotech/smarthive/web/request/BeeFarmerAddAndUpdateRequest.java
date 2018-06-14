package com.h1infotech.smarthive.web.request;

import java.util.Date;

import com.h1infotech.smarthive.domain.BeeFarmer;

public class BeeFarmerAddAndUpdateRequest {
	private Long id;
	private String name;
	private String username;
	private String address;
	private Date createDate;
	private Date updateDate;
	private String mobile;
	private String password;
	private Long organizationId;
	private String email;
	private Integer status;
	private Boolean firstTimeLogin;
	private Integer beeBoxNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(Boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	public Integer getBeeBoxNum() {
		return beeBoxNum;
	}

	public void setBeeBoxNum(Integer beeBoxNum) {
		this.beeBoxNum = beeBoxNum;
	}
	
	public BeeFarmer getBeeFarmerAdd() {
		BeeFarmer beeFarmer = new BeeFarmer();
		beeFarmer.setName(name);
		beeFarmer.setAddress(address); 
		beeFarmer.setCreateDate(new Date());
		beeFarmer.setUpdateDate(new Date());
		beeFarmer.setMobile(mobile);
		beeFarmer.setPassword(password);
		beeFarmer.setOrganizationId(organizationId); 
		beeFarmer.setEmail(email);
		beeFarmer.setStatus(status); 
		beeFarmer.setFirstTimeLogin(firstTimeLogin); 
		beeFarmer.setBeeBoxNum(0);
		return beeFarmer;
	}
	
	public BeeFarmer getBeeFarmerUpdate() {
		BeeFarmer beeFarmer = new BeeFarmer();
		beeFarmer.setId(id);
		beeFarmer.setName(name);
		beeFarmer.setUsername(username);
		beeFarmer.setAddress(address); 
		beeFarmer.setCreateDate(createDate);
		beeFarmer.setUpdateDate(new Date());
		beeFarmer.setMobile(mobile);
		beeFarmer.setPassword(password);
		beeFarmer.setOrganizationId(organizationId); 
		beeFarmer.setEmail(email);
		beeFarmer.setStatus(status); 
		beeFarmer.setFirstTimeLogin(firstTimeLogin); 
		beeFarmer.setBeeBoxNum(beeBoxNum);
		return beeFarmer;
	}

}
