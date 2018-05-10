package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.Partner;

import java.util.List;

public interface SmartHiveService {
    List<Partner> getPartners();

    BeeFarmer register(BeeFarmer farmer);
}
