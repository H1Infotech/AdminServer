package com.h1infotech.smarthive.domain;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "beeBoxGroupAssociation")
public class BeeBoxGroupAssociation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long beeBoxId;
	private Long groupId;
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBeeBoxId() {
		return beeBoxId;
	}

	public void setBeeBoxId(Long beeBoxId) {
		this.beeBoxId = beeBoxId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
