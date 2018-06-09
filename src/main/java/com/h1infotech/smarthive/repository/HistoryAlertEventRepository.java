package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.HistoryAlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryAlertEventRepository extends JpaRepository<HistoryAlertEvent, Long> {
	List<HistoryAlertEvent> findByAdminId(Long amdinId);
	List<HistoryAlertEvent> findByAdminIdIn(List<Long> amdinIds);
}
