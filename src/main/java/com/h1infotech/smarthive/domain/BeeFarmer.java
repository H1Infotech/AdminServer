package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "beeFarmer")
public class BeeFarmer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String username;
	private String address;
	private Date createDate;
	private Date updateDate;
	private String mobile;
	@JsonIgnore
	private String password;
	private Long organizationId;
	private String email;
	private Integer status;
	private Boolean firstTimeLogin;
	private Integer beeBoxNum;
	@Transient
	private String OrganizationName;

	@JsonIgnore
	public String getDesc() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String desc =  id+"_"+name+"_"+username
			           +"_"+address+"_"+mobile
				       +"_"+OrganizationName+"_"+email;
		if(createDate!=null) {
			desc+=formatter.format(createDate);
		}
		if(updateDate!=null) {
			desc+=formatter.format(updateDate);
		}
		return desc;
	}
	
	public Integer getBeeBoxNum() {
		return beeBoxNum;
	}

	public void setBeeBoxNum(Integer beeBoxNum) {
		this.beeBoxNum = beeBoxNum;
	}

	public String getOrganizationName() {
		return OrganizationName;
	}

	public void setOrganizationName(String organizationName) {
		OrganizationName = organizationName;
	}

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
}
