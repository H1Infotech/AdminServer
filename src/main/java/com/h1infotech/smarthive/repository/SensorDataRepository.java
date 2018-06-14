package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import com.h1infotech.smarthive.domain.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SensorDataRepository extends JpaRepository<SensorData, Long>{
	void deleteByFarmerIdIn(List<Long> ids);
	
	@Transactional
	void deleteByBeeBoxNoIn(List<String> beeBoxNos);
	
	List<SensorData> findByIdIn(List<Long> ids);
	
	List<SensorData> findByIdIn(List<Long> ids, Sort sort);

	List<SensorData> findByFarmerIdAndBeeBoxNoAndIdGreaterThan(Long farmerId, String boxId, Long id);
}
