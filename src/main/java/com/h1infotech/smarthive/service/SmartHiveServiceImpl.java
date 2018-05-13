package com.h1infotech.smarthive.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service(value = "smartHiveService")
@Transactional
public class SmartHiveServiceImpl implements SmartHiveService {
	
	@Autowired
    private PartnerRepository partnerRepository;
    
	@Autowired
    private BeeFarmerRepository beeFarmerRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<Partner> getPartners() {
        return partnerRepository.findAll();
    }

    @Override
    public BeeFarmer register(BeeFarmer farmer) {
        farmer.setPassword(bCryptPasswordEncoder.encode(farmer.getPassword()));
        return beeFarmerRepository.save(farmer);
    }
}
