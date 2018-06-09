package com.h1infotech.smarthive.domain;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Collection;
import javax.persistence.Id;
import java.util.stream.Stream;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.stream.Collectors;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "admin")
public class Admin implements Serializable, UserDetails {

	private static final long serialVersionUID = -1571097651968340577L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String username;
	private String mobile;
    @JsonIgnore
	private String password;
	private Date createDate;
	private Date updateDate;
	private Integer status;
	private Long organizationId;
	private String email;
	private Integer type;
	private String address;
	@Transient
	private String authToken;
    @Transient
    private String organizationName;
    @Transient
    private List<Integer> rights;
    
    @JsonIgnore
    public String getDesc() {
    	DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
    	
    	String desc = id+"_"+name+"_"+username
    			        +"_"+mobile+"_"+createDate==null?null:df3.format(createDate)
    			        +"_"+updateDate==null?null:df3.format(updateDate)+"_"+organizationName
    			        +"_"+email+"_"+address;
    	if(type==2) {
    		desc+="高级管理员";
    	}else if(type==3) {
    		desc+="组织管理员";
    	}else if(type==4) {
    		desc+="无组织管理员";
    	}
    	return desc;
    }
    
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public List<Integer> getRights() {
		return rights;
	}

	public void setRights(List<Integer> rights) {
		this.rights = rights;
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

	public void setUsername(String username) {
		this.username = username;
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
	
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
	
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isEnabled() {
        return true;
    }
    @Override
    @JsonIgnore
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of("USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
