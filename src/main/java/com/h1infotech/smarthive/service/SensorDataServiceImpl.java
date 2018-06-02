package com.h1infotech.smarthive.service;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.LinkedList;
import java.util.Collections;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.web.response.SensorDateRetrievalResponse;

@Service
public class SensorDataServiceImpl implements SensorDataService {

	@Autowired
	BeeBoxService beeBoxService;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Override
	public SensorData getLatestSensorData(Long latestSensorData) {
		Optional<SensorData>  sensorData = sensorDataRepository.findById(latestSensorData);
		if(sensorData.isPresent()) {
			return sensorData.get();
		}
		return null;
	}
	
	@Override
	public SensorDateRetrievalResponse getAllLatestSensorData(Long farmerId) {
		SensorDateRetrievalResponse response = new SensorDateRetrievalResponse();
		List<BeeBox> beeBoxes = beeBoxService.getBeeFamerBeeBoxes(farmerId);
		if(beeBoxes==null|| beeBoxes.size()==0) {
			return response;
		}
		response.setTotalBeeBoxNum(beeBoxes.size());
		for(BeeBox beeBox: beeBoxes) {
			if(beeBox.getProtectionStrategy()!=null && beeBox.getProtectionStrategy()) {
				response.setProtectionNum(response.getProtectionNum()+1);
			}
			if(beeBox.getStatus()!=null ) {
				if(beeBox.getStatus()==BeeBoxStatusEnum.RUNNING_STATUS.getStatus()) {
					response.setNormalBeeBoxNum(response.getNormalBeeBoxNum()+1);
					response.setRunningBeeBoxNum(response.getRunningBeeBoxNum()+1);
				}else if(beeBox.getStatus()==BeeBoxStatusEnum.OFFLINE_STATUS.getStatus()) {
					response.setOffLineBeeBoxNum(response.getOffLineBeeBoxNum()+1);
				}else if(beeBox.getStatus()==BeeBoxStatusEnum.ABNORMAL_STATUS.getStatus()) {
					response.setAbnormalBeeBoxNum(response.getAbnormalBeeBoxNum()+1);
				}
			}
		}
		int noProtectionNum = response.getTotalBeeBoxNum()-response.getProtectionNum()<0?0:response.getTotalBeeBoxNum()-response.getProtectionNum();
		response.setNoProtectionNum(noProtectionNum);

		Set<Long> beeBoxIds = new HashSet<Long>();
		List<Long> latestSensorDataIds = new LinkedList<Long>();
		for(BeeBox beeBox: beeBoxes) {
			beeBoxIds.add(beeBox.getId());
			if(beeBox.getLatestSensorDataId()!=null) {
				latestSensorDataIds.add(beeBox.getLatestSensorDataId());
			}
		}
		List<SensorData> allSensorData = sensorDataRepository.findByIdIn(latestSensorDataIds);
		if(allSensorData.size() > 0) {
			for(SensorData sensorData: allSensorData) {
				if(beeBoxIds.contains(sensorData.getId())) {
					beeBoxIds.remove(sensorData.getId());
				}
			}
		}
		if(beeBoxIds.size()>0) {
			for(Long id: beeBoxIds) {
				SensorData sensorData = new SensorData();
				sensorData.setBoxId(id);
				sensorData.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
				allSensorData.add(sensorData);
			}
		}
		Collections.sort(allSensorData);
		response.setLatestSensorData(allSensorData);
		return response;
	}
	
}
