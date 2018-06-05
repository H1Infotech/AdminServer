package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.web.response.BeeBoxPageRetrievalResponse;

public interface BeeBoxService {
    List<BeeBox> getBeeFamerBeeBoxes(long id);
    BeeBox getBeeBoxByTokenAndId(String tocken, Long id);
    BeeBox getBeeBoxByUserNameAndId(String userName, Long id);
    Object addBeeBox(String userName, BeeBox beeBox);
    void deleteBeeBoxByIdIn(List<Long> ids);
    
    
    List<BeeBox> getAllBeeBoxes();
    BeeBoxPageRetrievalResponse getAllBeeBoxes(int pageNo, int pageSize);
}
