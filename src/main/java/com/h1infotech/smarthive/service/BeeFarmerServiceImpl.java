package com.h1infotech.smarthive.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;

@Service
public class BeeFarmerServiceImpl implements BeeFarmerService {
	
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	
	public BeeFarmer getBeeFarmerByUserName(String userName) {
		if(StringUtils.isEmpty(userName)) {
			return null;
		}
		try {
			return beeFarmerRepository.findDistinctFirstByName(userName);
		} catch(Exception e) {
			throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
		}
	}
}
