package com.h1infotech.smarthive.web.request;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBoxGroup;

public class SaveBeeBoxGroupRequest {
	private BeeBoxGroup beeBoxGroup;
	private List<Long> beeBoxIds;
	
	public BeeBoxGroup getBeeBoxGroup() {
		return beeBoxGroup;
	}
	public void setBeeBoxGroup(BeeBoxGroup beeBoxGroup) {
		this.beeBoxGroup = beeBoxGroup;
	}
	public List<Long> getBeeBoxIds() {
		return beeBoxIds;
	}
	public void setBeeBoxIds(List<Long> beeBoxIds) {
		this.beeBoxIds = beeBoxIds;
	}
}
