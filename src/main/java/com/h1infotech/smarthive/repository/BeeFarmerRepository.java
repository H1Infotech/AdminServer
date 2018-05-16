package com.h1infotech.smarthive.repository;

import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeeFarmerRepository extends JpaRepository<BeeFarmer, Long> {
    BeeFarmer findDistinctFirstByName(String name);
    
    @Query(value = "update beeFarmer set password= :password, firstTimeLogin= :firstTimeLogin where name= :userName", nativeQuery = true)  
    @Modifying
    void updatePassword(@Param(value = "userName")String userName, @Param(value = "password")String password, @Param(value = "firstTimeLogin")boolean firstTimeLogin);
}
