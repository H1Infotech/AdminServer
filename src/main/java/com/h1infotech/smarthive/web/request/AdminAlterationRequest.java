package com.h1infotech.smarthive.web.request;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.AdminRight;

public class AdminAlterationRequest {

	private Long id;
	private String name;
	private String username;
	private String mobile;
	private Date createDate;
	private Date updateDate;
	private Integer status;
	private Long organizationId;
	private String email;
	private Integer type;
	private String address;
	private List<Integer> rights;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Integer> getRights() {
		return rights;
	}

	public void setRights(List<Integer> rights) {
		this.rights = rights;
	}
	
	public Admin getAdmin() {
		Admin admin = new Admin();
		admin.setId(id); 
		admin.setName(name);
		admin.setUsername(username); 
		admin.setMobile(mobile); 
		admin.setCreateDate(new Date()); 
		admin.setUpdateDate(new Date());
		admin.setStatus(status);
		admin.setOrganizationId(organizationId); 
		admin.setEmail(email); 
		admin.setType(type);
		admin.setAddress(address); 
		return admin;
	}
	
	public List<AdminRight> getAdminRight(){
		List<AdminRight> rightRights = new LinkedList<AdminRight>();
		if(rights==null || rights.size()==0) {
			return null;
		}
		AdminRight adminRight = null;
		for(Integer right: rights) {
			adminRight = new AdminRight();
			adminRight.setAdminId(id);
			adminRight.setRightId(right);
			adminRight.setCreateDate(createDate);
			rightRights.add(adminRight);
		}
		return rightRights;
	}
}
