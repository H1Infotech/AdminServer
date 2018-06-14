package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.HistoryAlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HistoryAlertEventRepository extends JpaRepository<HistoryAlertEvent, Long> {
	@Transactional
	void deleteByBeeBoxIdIn(List<Long> ids);
	List<HistoryAlertEvent> findByAdminId(Long amdinId);
	List<HistoryAlertEvent> findByAdminIdIn(List<Long> amdinIds);
}
