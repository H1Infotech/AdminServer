package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.Partner;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
public interface SmartHiveService {
    List<Partner> getPartners();

    BeeFarmer register(BeeFarmer farmer);
}
