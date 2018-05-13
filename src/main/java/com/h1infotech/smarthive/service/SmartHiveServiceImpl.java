package com.h1infotech.smarthive.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.web.SmartHiveController;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;

@Service(value = "smartHiveService")
@Transactional
public class SmartHiveServiceImpl implements SmartHiveService {
    
	private Logger logger = LoggerFactory.getLogger(SmartHiveController.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private BeeBoxRepository beeBoxRepository;
	
	@Autowired
    private PartnerRepository partnerRepository;
	
	@Autowired
	private BeeFarmerRepository beeFarmerRepository;
	
    @Override
    public List<Partner> getPartners() {
        return partnerRepository.findAll();
    }
    
    @Override
	public List<BeeBox> getBeeBox(String token) {
    	if(StringUtils.isEmpty(token)) {
    		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    	}
    	
    	String userName = jwtTokenUtil.getUsernameFromToken(token);
    	logger.info("====User Name: " + userName + "====");
    	if(StringUtils.isEmpty(userName)) {
    		throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    	}
    	
    	BeeFarmer beeFarmer = null;
    	try {
    		beeFarmer = beeFarmerRepository.findDistinctFirstByName(userName);    	
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
    	}
    	
        if(beeFarmer == null || beeFarmer.getId() == null) {
        	throw new BusinessException(BizCodeEnum.NO_USER_INFO);
        }
        
        try {	
        	return beeBoxRepository.findBeeBoxesByFarmerId(beeFarmer.getId());
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
    	}
    }
}
