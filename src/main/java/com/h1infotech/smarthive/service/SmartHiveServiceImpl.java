package com.h1infotech.smarthive.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "smartHiveService")
@Transactional
public class SmartHiveServiceImpl implements SmartHiveService {
    
	@Autowired
    private PartnerRepository partnerRepository;
	
    @Override
    public List<Partner> getPartners() {
        return partnerRepository.findAll();
    }

}
