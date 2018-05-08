package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.Partner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "smartHiveService")
public class SmartHiveServiceImpl implements SmartHiveService {
    @Override
    public List<Partner> getPartners() {
        return null;
    }
}
