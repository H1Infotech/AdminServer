package com.h1infotech.smarthive.web.request;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;

public class BeeBoxSensorDataRequest {
	private List<SensorDataTag> sensorDataTags;

	public List<SensorDataTag> getSensorDataTags() {
		return sensorDataTags;
	}

	public void setSensorDataTags(List<SensorDataTag> sensorDataTags) {
		this.sensorDataTags = sensorDataTags;
	}

	public List<String> getBeeBoxNos() {
		if(sensorDataTags==null || sensorDataTags.size()==0) {
			return null;
		}
		
		List<String> beeBoxNos = new LinkedList<String>();

		for(SensorDataTag sensorDataTag: sensorDataTags) {
			if(sensorDataTag!=null && !StringUtils.isEmpty(sensorDataTag.getBeeBoxNo())) {
				beeBoxNos.add(sensorDataTag.getBeeBoxNo());
			}
		}
		return beeBoxNos;
	}
	
	public Map<String, Long> getSensorDataTag() {
		if(sensorDataTags==null || sensorDataTags.size()==0) {
			return null;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		for(SensorDataTag sensorDataTag: sensorDataTags) {
			if(sensorDataTag!=null && !StringUtils.isEmpty(sensorDataTag.getBeeBoxNo())) {
				map.put(sensorDataTag.getBeeBoxNo(), sensorDataTag.getLastestSensorDataId());
			}
		}
		return map;
	}
}
