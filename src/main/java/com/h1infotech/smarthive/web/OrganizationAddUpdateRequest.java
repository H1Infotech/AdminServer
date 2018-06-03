package com.h1infotech.smarthive.web;

import java.util.Date;

import com.h1infotech.smarthive.domain.Organization;

public class OrganizationAddUpdateRequest {
	private Long id;
	private String organizationName;
	private String email;
	private Integer memberNum;
	private String contactName;
	private String contactPhone;
	private Date createDate;
	private Date updateDate;
	private Integer status;
	private String address;
	private Long adminId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Organization getAddOrganization() {
		Organization organization = new Organization();
		organization.setOrganizationName(organizationName);
		organization.setEmail(email);
		organization.setMemberNum(memberNum);
		organization.setContactName(contactName);
		organization.setContactPhone(contactPhone);
		organization.setCreateDate(createDate);
		organization.setUpdateDate(new Date());
		organization.setStatus(status);
		organization.setAddress(address);
		organization.setAdminId(adminId);
		return organization;
	}
	public Organization getUpdateOrganization() {
		Organization organization = new Organization();
		organization.setId(id);
		organization.setOrganizationName(organizationName);
		organization.setEmail(email);
		organization.setMemberNum(memberNum);
		organization.setContactName(contactName);
		organization.setContactPhone(contactPhone);
		organization.setCreateDate(createDate);
		organization.setUpdateDate(new Date());
		organization.setStatus(status);
		organization.setAddress(address);
		organization.setAdminId(adminId);
		return organization;
	}
}
