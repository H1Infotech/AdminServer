package com.h1infotech.smarthive.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "smartHiveService")
@Transactional
public class SmartHiveServiceImpl implements SmartHiveService {
    
	@Autowired
    private OrganizationRepository partnerRepository;
	
    @Override
    public List<Organization> getPartners() {
        return partnerRepository.findAll();
    }

}
