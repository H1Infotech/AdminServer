package com.h1infotech.smarthive.common;

import com.alibaba.fastjson.JSONObject;

public class BasePOJO {
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
