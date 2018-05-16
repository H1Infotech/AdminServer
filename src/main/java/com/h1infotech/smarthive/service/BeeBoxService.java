package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;

public interface BeeBoxService {
    List<BeeBox> getBeeBox(String token);
    Object addBeeBox(String userName, BeeBox beeBox);
    void deleteBeeBoxByIdIn(List<Long> ids);
}