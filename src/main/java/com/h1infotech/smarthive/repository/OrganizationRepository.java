package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.h1infotech.smarthive.domain.Organization;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long> {
	
	Organization findByIdAndAdminId(long id, long adminId);
	List<Organization> findByIdIn(List<Long> ids);
	List<Organization> findByAdminId(long adminId);
	List<Organization> findByAdminId(long adminId, Sort sort);
	Page<Organization> findByAdminId(long adminId, Pageable page);
	int deleteByIdIn(List<Long> ids);
	@Transactional
	List<Organization> deleteByAdminIdIn(List<Long> ids);
	int deleteByAdminIdIsAndIdIn(Long adminId, List<Long> ids);
}
