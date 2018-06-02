package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeBox;

public interface BeeBoxService {
    List<BeeBox> getBeeFamerBeeBoxes(long id);
    List<BeeBox> getBeeBox(String token);
    BeeBox getBeeBoxByTokenAndId(String tocken, Long id);
    BeeBox getBeeBoxByUserNameAndId(String userName, Long id);
    Object addBeeBox(String userName, BeeBox beeBox);
    void deleteBeeBoxByIdIn(List<Long> ids);
}
