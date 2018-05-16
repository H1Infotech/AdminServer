package com.h1infotech.smarthive.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;

@Service(value = "beeBoxService")
public class BeeBoxServiceImpl implements BeeBoxService {
	
	private Logger logger = LoggerFactory.getLogger(BeeBoxServiceImpl.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private BeeBoxRepository beeBoxRepository;
	
	@Autowired
	private BeeFarmerRepository beeFarmerRepository;
	
    @Override
	public List<BeeBox> getBeeBox(String token) {
    	
    	BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(token);
    	if(beeFarmer == null|| beeFarmer.getId() == null) {
    		throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    	}
        
        try {	
        	return beeBoxRepository.findBeeBoxesByFarmerId(beeFarmer.getId());
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
    	}
    }
    
    public Object addBeeBox(String token, BeeBox beeBox) {
    	
    	if(beeBox==null) {
    		throw new BusinessException(BizCodeEnum.ADD_EMPTY_BEEBOX);
    	}
    	BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(token);
    	if(beeFarmer == null|| beeFarmer.getId() == null) {
    		throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    	}
        
        try {	
        	return beeBoxRepository.save(beeBox);
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
    	}
    }
    
    public void deleteBeeBoxByIdIn(List<Long> ids) {
    	try {
    		beeBoxRepository.deleteByIdIn(ids);
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
    	}
    }
    
    public void deleteBeeBoxByFarmerIdAndIdIn(String token, List<Long> ids) {
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
    		beeBoxRepository.deleteBeeBoxByFarmerIdAndIdIn(beeFarmer.getId(), ids);
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
    	}
    }
}
