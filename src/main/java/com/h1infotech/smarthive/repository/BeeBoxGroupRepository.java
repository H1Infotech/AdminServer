package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BeeBoxGroupRepository extends JpaRepository<BeeBoxGroup, Long> {
	List<BeeBoxGroup> findByAdminId(Long id,Sort sort);
	@Transactional
	void deleteByIdIn(List<Long> ids);
	@Transactional
	List<BeeBoxGroup> deleteByAdminIdIn(List<Long> adminIds);
	List<BeeBoxGroup> findByIdIn(List<Long> ids);
	BeeBoxGroup findByAdminIdAndGroupName(Long adminId, String groupName);
}
