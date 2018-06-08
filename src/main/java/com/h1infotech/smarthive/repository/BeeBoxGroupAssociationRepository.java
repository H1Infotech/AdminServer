package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;

public interface BeeBoxGroupAssociationRepository extends JpaRepository<BeeBoxGroupAssociation, Long> {
	List<BeeBoxGroupAssociation> findByBeeBoxId(Long beeBoxId);
	void deleteByGroupIdIn(List<Long> ids);
}
