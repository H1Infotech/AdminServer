package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepository extends JpaRepository<SensorData, Long>{
	void deleteByFarmerIdIn(List<Long> ids);
	
	List<SensorData> findByIdIn(List<Long> ids);

	List<SensorData> findByFarmerIdAndBeeBoxNoAndIdGreaterThan(Long farmerId, String boxId, Long id);
}
