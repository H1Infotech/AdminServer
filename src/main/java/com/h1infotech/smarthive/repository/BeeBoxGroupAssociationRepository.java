package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;

public interface BeeBoxGroupAssociationRepository extends JpaRepository<BeeBoxGroupAssociation, Long> {
	List<BeeBoxGroupAssociation> findByBeeBoxId(Long beeBoxId);
	List<BeeBoxGroupAssociation> findByGroupId(Long groupId);
	@Transactional
	void deleteByGroupIdIn(List<Long> ids);
	@Transactional
	List<BeeBoxGroupAssociation> deleteByBeeBoxIdIn(List<Long> ids);
}
