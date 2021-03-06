package com.h1infotech.smarthive.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import com.h1infotech.smarthive.domain.BeeBox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeeBoxRepository extends JpaRepository<BeeBox, Long>, JpaSpecificationExecutor<BeeBox> {
	BeeBox findFirstByFarmerId(Long farmerId);
	BeeBox findByBeeBoxNo(String beeBoxNo);
	List<BeeBox> findBeeBoxesByFarmerId(Long farmerId);
	List<BeeBox> findByFarmerIdIn(List<Long> ids);
	List<BeeBox> findByFarmerIdIn(List<Long> ids, Sort sort);
	List<BeeBox> findByIdIn(List<Long> ids);
	List<BeeBox> findByBeeBoxNoIn(List<String> numbers);
	Page<BeeBox> findByFarmerIdIn(List<Long> ids, Pageable page);
	BeeBox findBeeBoxByIdAndFarmerId(Long id, Long farmerId);
	@Transactional
	List<BeeBox> deleteByIdIn(List<Long> ids);
	void deleteByFarmerIdIn(List<Long> farmerIds);
	BeeBox findByLatAndLng(BigDecimal lat, BigDecimal lng);
	void deleteBeeBoxByFarmerIdAndIdIn(Long farmerId, List<Long> ids);
	
}
