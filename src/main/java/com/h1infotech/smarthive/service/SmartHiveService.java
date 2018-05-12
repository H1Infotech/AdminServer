package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.domain.BeeFarmer;

public interface SmartHiveService {
    List<Partner> getPartners();

    BeeFarmer register(BeeFarmer farmer);
}
