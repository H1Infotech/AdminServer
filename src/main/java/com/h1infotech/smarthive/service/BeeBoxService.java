package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.web.response.BeeBoxPageRetrievalResponse;

public interface BeeBoxService {    
    List<BeeBox> getAllBeeBoxes();
    BeeBoxPageRetrievalResponse getAllBeeBoxes(int pageNo, int pageSize);
}
