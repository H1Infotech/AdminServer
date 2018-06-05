package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.AdminRight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRightRepository extends JpaRepository<AdminRight, Long> {
	List<AdminRight> findByAdminIdIs(Long adminId);
	void deleteByAdminId(Long id);
}
