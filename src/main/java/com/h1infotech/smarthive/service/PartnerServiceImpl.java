package com.h1infotech.smarthive.service;

import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "partnerService")
public class PartnerServiceImpl implements PartnerService {

	Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);
	
	@Autowired
	PartnerRepository PartnerRepository;
	
	@Override
	public Partner getParterById(Long id) {
		Optional<Partner> partner = null;
		try {
			partner = PartnerRepository.findById(id);
		} catch(Exception e) {
			return null;
		}
		return partner.isPresent()?partner.get():null;
	}

}
