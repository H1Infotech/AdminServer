package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByAdminId(Long adminId);
	List<Event> findByAdminIdIn(List<Long> adminIds);
	@Transactional
	void deleteByIdIn(List<Long> ids);
}
