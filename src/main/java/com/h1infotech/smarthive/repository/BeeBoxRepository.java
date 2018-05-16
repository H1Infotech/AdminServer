package com.h1infotech.smarthive.repository;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeeBoxRepository extends JpaRepository<BeeBox, Long>{
	List<BeeBox> findBeeBoxesByFarmerId(Long farmerId);
}
