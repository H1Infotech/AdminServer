package com.h1infotech.smarthive.web.request;

import java.util.List;

public class QueryGroupBeeBoxRequest {
	private List<FilterItem> filterItems;

	public List<FilterItem> getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(List<FilterItem> filterItems) {
		this.filterItems = filterItems;
	}
}
