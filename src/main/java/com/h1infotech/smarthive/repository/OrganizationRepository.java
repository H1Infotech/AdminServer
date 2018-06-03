package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.domain.Organization;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long> {
	List<Organization> findByAdminId(long adminId);
	Page<Organization> findByAdminId(long adminId, Pageable page);
}
