package com.h1infotech.smarthive.repository;

import java.util.Date;
import java.util.List;
import com.h1infotech.smarthive.domain.IntervalSensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IntervalSensorDataRepository extends JpaRepository<IntervalSensorData, Long> {
	void deleteByFarmerIdIn(List<Long> ids);
	@Transactional
	void deleteByBeeBoxNoIn(List<String> beeBoxNos);
	List<IntervalSensorData> findByBeeBoxNoIsAndCreateDateBetweenOrderByCreateDateAsc(String beeBoxNo, Date startDate, Date endDate);
}
