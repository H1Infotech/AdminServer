package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeeBoxGroupRepository extends JpaRepository<BeeBoxGroup, Long> {
	List<BeeBoxGroup> findByAdminId(Long id);
	void deleteByIdIn(List<Long> ids);
	List<BeeBoxGroup> deleteByAdminIdIn(List<Long> adminIds);
	List<BeeBoxGroup> findByIdIn(List<Long> ids);
	BeeBoxGroup findByAdminIdAndGroupName(Long adminId, String groupName);
}
