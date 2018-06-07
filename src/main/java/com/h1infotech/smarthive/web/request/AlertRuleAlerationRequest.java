package com.h1infotech.smarthive.web.request;

import java.util.Date;

import com.h1infotech.smarthive.domain.Event;

public class AlertRuleAlerationRequest {
	private Long id;
	private String ruleName;
	private Long groupId;
	private String action;
	private Double minThreshold;
	private Double maxThreshold;
	private Integer ruleType;
	private String notificationTarget;
	private String notificationWay;
	private Date createDate;
	private Long adminId;

	public Event getEvent() {
		Event event = new Event();
		event.setId(id);
		event.setRuleName(ruleName);
		event.setGroupId(groupId);
		event.setAction(action);
		event.setMinThreshold(minThreshold);
		event.setMaxThreshold(maxThreshold);
		event.setRuleType(ruleType);
		event.setNotificationTarget(notificationTarget);
		event.setNotificationWay(notificationWay);
		event.setCreateDate(new Date());
		event.setAdminId(adminId);
		return event;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getMinThreshold() {
		return minThreshold;
	}

	public void setMinThreshold(Double minThreshold) {
		this.minThreshold = minThreshold;
	}

	public Double getMaxThreshold() {
		return maxThreshold;
	}

	public void setMaxThreshold(Double maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public String getNotificationTarget() {
		return notificationTarget;
	}

	public void setNotificationTarget(String notificationTarget) {
		this.notificationTarget = notificationTarget;
	}

	public String getNotificationWay() {
		return notificationWay;
	}

	public void setNotificationWay(String notificationWay) {
		this.notificationWay = notificationWay;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
}
