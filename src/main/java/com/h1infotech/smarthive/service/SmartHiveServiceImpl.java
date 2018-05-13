package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
