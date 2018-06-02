package com.h1infotech.smarthive.service;

import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "partnerService")
public class OrganizationServiceImpl implements OrganizationService {

	Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	OrganizationRepository PartnerRepository;
	
	@Override
	public Organization getOrganizationById(Long id) {
		Optional<Organization> partner = null;
		try {
			partner = PartnerRepository.findById(id);
		} catch(Exception e) {
			return null;
		}
		return partner.isPresent()?partner.get():null;
	}

}
