package com.h1infotech.smarthive.repository;

import com.h1infotech.smarthive.domain.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Admin findDistinctFirstByUsername(String username);

	@Query(value = "update admin set password= :password where username= :username", nativeQuery = true)
	@Modifying
	void updatePassword(@Param(value = "username") String username, @Param(value = "password") String password);
}
