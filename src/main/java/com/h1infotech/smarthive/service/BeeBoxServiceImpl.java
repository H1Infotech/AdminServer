package com.h1infotech.smarthive.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.h1infotech.smarthive.web.response.BeeBoxPageRetrievalResponse;

@Service(value = "beeBoxService")
public class BeeBoxServiceImpl implements BeeBoxService {

	@Autowired
	private BeeBoxRepository beeBoxRepository;
	
	@Override
    public List<BeeBox> getAllBeeBoxes() {
		return beeBoxRepository.findAll();
    }
	
	@Override 
	public BeeBoxPageRetrievalResponse getAllBeeBoxes(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BeeBox> pageBeeBox = beeBoxRepository.findAll(page);
		BeeBoxPageRetrievalResponse response = new BeeBoxPageRetrievalResponse();
		if(pageBeeBox==null || pageBeeBox.getContent()==null) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}else {
			response.setBeeBoxes(pageBeeBox.getContent());
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(pageBeeBox.getTotalPages());
		}
		return response;
	}
	
	
	
	@Override
	public List<BeeBox> getBeeFamerBeeBoxes(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeeBox getBeeBoxByTokenAndId(String tocken, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeeBox getBeeBoxByUserNameAndId(String userName, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addBeeBox(String userName, BeeBox beeBox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBeeBoxByIdIn(List<Long> ids) {
		// TODO Auto-generated method stub
		
	}
	
//	private Logger logger = LoggerFactory.getLogger(BeeBoxServiceImpl.class);
//	
//	@Autowired
//	private JwtTokenUtil jwtTokenUtil;
//	
//	@Autowired
//	private BeeBoxRepository beeBoxRepository;
//	
//	@Autowired
//	private BeeFarmerRepository beeFarmerRepository;
//	
//    @Override
//	public List<BeeBox> getBeeFamerBeeBoxes(long farmerId) {     
//        try {	
//        	return beeBoxRepository.findBeeBoxesByFarmerId(farmerId);
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
//    	}
//    }
//	
//    @Override
//	public List<BeeBox> getBeeBox(String token) {
//        
//        try {	
//        	return beeBoxRepository.findBeeBoxesByFarmerId(null);
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
//    	}
//    }
//    
////    public BeeBox getBeeBoxByTokenAndId(String token, Long id) {
////    	
////    	if(StringUtils.isEmpty(token) || id == null) {
////    		return null;
////    	}
////    	try {
////    		//BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(token);
////    		logger.info("====Farmer Id: " + JSONObject.toJSONString(beeFarmer)+"====");
////    		if(beeFarmer != null && beeFarmer.getId()!=null) {
////    			return beeBoxRepository.findBeeBoxByIdAndFarmerId(id, beeFarmer.getId());
////    		}
////    	} catch(Exception e) {
////    		logger.error("===Get Bee Box Information Error(Bee Box ID:{})====",id);	
////    	}
////    	return null;
////    }
//    
//    public BeeBox getBeeBoxByUserNameAndId(String userName, Long id) {
//    	if(StringUtils.isEmpty(userName) || id == null) {
//    		return null;
//    	}
//    	try {
//    		BeeFarmer beeFarmer = beeFarmerRepository.findDistinctFirstByName(userName);
//    		if(beeFarmer != null && beeFarmer.getId()!=null) {
//    			return beeBoxRepository.findBeeBoxByIdAndFarmerId(id, beeFarmer.getId());
//    		}
//    	}catch(Exception e) {
//    		logger.error("====Get Bee Box Information Error(User Name: {}, Bee Box ID:{})====",userName,id);
//    	}
//    	return null;
//    }
//    
//    public Object addBeeBox(String token, BeeBox beeBox) {
//    	
//    	if(beeBox==null) {
//    		throw new BusinessException(BizCodeEnum.ADD_EMPTY_BEEBOX);
//    	}
//    	BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(token);
//    	if(beeFarmer == null|| beeFarmer.getId() == null) {
//    		throw new BusinessException(BizCodeEnum.NO_USER_INFO);
//    	}
//        
//        try {	
//        	return beeBoxRepository.save(beeBox);
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
//    	}
//    }
//    
//    public void deleteBeeBoxByIdIn(List<Long> ids) {
//    	try {
//    		beeBoxRepository.deleteByIdIn(ids);
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
//    	}
//    }
//    
//    public void deleteBeeBoxByFarmerIdAndIdIn(String token, List<Long> ids) {
//    	if(StringUtils.isEmpty(token)) {
//    		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
//    	}
//    	
//    	String userName = jwtTokenUtil.getUsernameFromToken(token);
//    	if(StringUtils.isEmpty(userName)) {
//    		throw new BusinessException(BizCodeEnum.NO_USER_INFO);
//    	}
//    	
//    	BeeFarmer beeFarmer = null;
//    	try {
//    		beeFarmer = beeFarmerRepository.findDistinctFirstByName(userName);    	
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
//    	}
//    	
//        if(beeFarmer == null || beeFarmer.getId() == null) {
//        	throw new BusinessException(BizCodeEnum.NO_USER_INFO);
//        }
//    	
//    	try {
//    		beeBoxRepository.deleteBeeBoxByFarmerIdAndIdIn(beeFarmer.getId(), ids);
//    	}catch(Exception e) {
//    		throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
//    	}
//    }
}
