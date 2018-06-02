package com.h1infotech.smarthive.repository;

import com.h1infotech.smarthive.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
