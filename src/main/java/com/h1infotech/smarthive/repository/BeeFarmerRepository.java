package com.h1infotech.smarthive.repository;

import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BeeFarmerRepository extends PagingAndSortingRepository<BeeFarmer, Long> {
	BeeFarmer findByIdAndOrganizationId(Long id, Long organizationId);
    BeeFarmer findDistinctFirstByUsername(String username);
    List<BeeFarmer> findByOrganizationId(Long organizationId);
    List<BeeFarmer> findByOrganizationIdIn(List<Long> ids);
    Page<BeeFarmer> findByOrganizationIdIsNull(Pageable page);
    List<BeeFarmer> findByOrganizationIdIsNull();
    Page<BeeFarmer> findByOrganizationIdIn(List<Long> ids, Pageable page);
    void deleteByIdIn(List<Long> ids);
    @Query(value = "update beeFarmer set organizationId = null where organizationId in: organizationIds", nativeQuery = true)  
    @Modifying
    void wipeOutOrganizationId(@Param(value = "organizationIds")List<Long> organizationIds);
    @Query(value = "update beeFarmer set password= :password, firstTimeLogin= :firstTimeLogin where name= :userName", nativeQuery = true)  
    @Modifying
    void updatePassword(@Param(value = "userName")String userName, @Param(value = "password")String password, @Param(value = "firstTimeLogin")boolean firstTimeLogin);
}
