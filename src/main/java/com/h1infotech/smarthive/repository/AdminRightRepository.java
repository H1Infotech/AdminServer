package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.AdminRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AdminRightRepository extends JpaRepository<AdminRight, Long> {
	List<AdminRight> findByAdminIdIs(Long adminId);
	@Transactional
	void deleteByAdminId(Long id);
	@Transactional
	void deleteByAdminIdIn(List<Long> ids);
}
