package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "smartHiveService")
public class SmartHiveServiceImpl implements SmartHiveService {
    @Autowired
    private BeeFarmerRepository beeFarmerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
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
