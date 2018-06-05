package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeeBoxRepository extends JpaRepository<BeeBox, Long> {
	List<BeeBox> findBeeBoxesByFarmerId(Long farmerId);
	List<BeeBox> findByFarmerIdIn(List<Long> ids);
	List<BeeBox> findByIdIn(List<Long> ids);
	Page<BeeBox> findByFarmerIdIn(List<Long> ids, Pageable page);
	BeeBox findBeeBoxByIdAndFarmerId(Long id, Long farmerId);
	void deleteByIdIn(List<Long> ids);
	void deleteByFarmerIdIn(List<Long> farmerIds);
	void deleteBeeBoxByFarmerIdAndIdIn(Long farmerId, List<Long> ids);
}
