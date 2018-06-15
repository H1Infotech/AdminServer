package com.h1infotech.smarthive.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdminRepository extends PagingAndSortingRepository<Admin, Long> {
	Admin findDistinctFirstByUsername(String username);
	void deleteByIdIn(List<Long> ids);
	Page<Admin> findByTypeNotAndIdNot(Integer type, long excludeId, Pageable page);
	List<Admin> findByTypeNotAndIdNot(Integer type, long excludeId);
	List<Admin> findByTypeNotAndIdNot(Integer type, long excludeId, Sort sort);
	List<Admin> findByTypeIn(List<Integer> types);
	List<Admin> findByTypeIn(List<Integer> types, Sort sort);
	@Query(value = "update admin set password= :password where username= :username", nativeQuery = true)
	@Modifying
	void updatePassword(@Param(value = "username") String username, @Param(value = "password") String password);
}
