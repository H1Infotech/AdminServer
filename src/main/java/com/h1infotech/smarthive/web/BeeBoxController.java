package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import java.util.Optional;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Sort;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.domain.SensorData;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.service.BeeBoxService;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.domain.IntervalSensorData;
import org.springframework.web.bind.annotation.GetMapping;
import com.h1infotech.smarthive.service.SensorDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.web.request.BeeBoxAddRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.service.IntervalSensorDataService;
import com.h1infotech.smarthive.web.response.OverviewDataResponse;
import com.h1infotech.smarthive.web.request.BeeBoxDeletionRequest;
import com.h1infotech.smarthive.web.request.BeeBoxRetrievalRequest;
import com.h1infotech.smarthive.web.request.ChartSensoeDataRequest;
import com.h1infotech.smarthive.web.request.AmbiguousSearchRequest;
import com.h1infotech.smarthive.web.request.BeeBoxSensorDataRequest;
import com.h1infotech.smarthive.web.request.BeeBoxPageRetrievalRequest;
import com.h1infotech.smarthive.web.response.BeeBoxPageRetrievalResponse;

@RestController
@RequestMapping("/api")
public class BeeBoxController {
	
	private Logger logger = LoggerFactory.getLogger(BeeBoxController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	BeeBoxService beeBoxService;
	
	@Autowired
	BeeFarmerService beeFarmerService;
	
	@Autowired
	BeeBoxRepository beeBoxRepository;
	
    @Autowired
    SensorDataService sensorDataService;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Autowired
	IntervalSensorDataService intervalSensorDataService;
	
	@PostMapping(path = "/getAllBeeBoxSensorData")
	@ResponseBody
	public Response<List<SensorData>> getAllBeeBoxSensorData(HttpServletRequest httpRequest, @RequestBody AmbiguousSearchRequest request) {
		try {
			if (request == null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			logger.info("====Catching the Request for Getting BeeBox Sensor Data====");
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			List<BeeBox> beeBoxes = null;
			List<Long> beeFarmerIds = null;
			List<Long> organizationIds = null;
			switch (AdminTypeEnum.getEnum(admin.getType())) {
				case SUPER_ADMIN:
				case SENIOR_ADMIN:
					beeBoxes = beeBoxService.getAllBeeBoxes();
					break;
				case ORGANIZATION_ADMIN:
					organizationIds = organizationService.getIdsByAdminId(admin.getId());
					beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
					beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
					break;
				case NO_ORGANIZATION_ADMIN:
					beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
					beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
					break;
			}
			if (beeBoxes == null || beeBoxes.size() == 0) {
				return Response.success(null);
			}
			
    		if(!StringUtils.isEmpty(request.getKeyword())) {
    			if(beeBoxes==null || beeBoxes.size()==0) {
    				Iterator<BeeBox> iterator = beeBoxes.iterator();
    				while(iterator.hasNext()) {
    					if(iterator.next().getDesc().indexOf(request.getKeyword())==-1) {
    						iterator.remove();
    					}
    				}
    			}
    		}
			
			List<Long> ids = new LinkedList<Long>();
			List<SensorData> sensorData = new LinkedList<SensorData>();
			for (BeeBox beeBox : beeBoxes) {
				if (beeBox.getLatestSensorDataId() == null) {
					SensorData data = new SensorData();
					data.setBeeBoxNo(beeBox.getBeeBoxNo());
					data.setStatus(3);
					sensorData.add(data);
				} else {
					ids.add(beeBox.getLatestSensorDataId());
				}
			}
			List<SensorData> existSensorData = sensorDataService.getSensorDara(ids);
			sensorData.addAll(existSensorData);
			Collections.sort(sensorData);
			return Response.success(sensorData);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Get BeeBox Info Error", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}  
//    @PostMapping(path = "/getBeeBoxeSensorData")
//    @ResponseBody
//    public Response<List<SensorData>> getBeeBoxes(HttpServletRequest httpRequest) {
//    	try {
//    		if(request == null) {
//    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
//    		}
//    		logger.info("====Catching the Request for Getting beeBox: {}====", JSONObject.toJSONString(request));
//    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
//    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
//    		if(admin==null) {
//    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
//    		}
//    		Sort sort = null;
//    		List<BeeBox> beeBoxes = null;
//    		List<Long> beeFarmerIds = null;
//    		List<Long> organizationIds = null;
//    		switch(AdminTypeEnum.getEnum(admin.getType())) {
//    			case SUPER_ADMIN:
//    			case SENIOR_ADMIN:
//    				beeBoxes = beeBoxService.getAllBeeBoxes();
//    				break;
//    			case ORGANIZATION_ADMIN:
//    				organizationIds = organizationService.getIdsByAdminId(admin.getId());
//    				beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
//    				sort = new Sort(Sort.Direction.ASC,"id");
//    				if(beeFarmerIds!=null && beeFarmerIds.size()>0){
//    					beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds, sort);
//    				}
//    				break;
//    			case NO_ORGANIZATION_ADMIN:
//    				beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
//    				sort = new Sort(Sort.Direction.ASC,"id");
//    				if(beeFarmerIds!=null && beeFarmerIds.size()>0){
//    					beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds, sort);
//    				}
//    				break;
//    		}
//    		if(StringUtils.isEmpty(request.getKeyword())) {
//    			if(beeBoxes==null || beeBoxes.size()==0) {
//    				Iterator<BeeBox> iterator = beeBoxes.iterator();
//    				while(iterator.hasNext()) {
//    					if(iterator.next().getDesc().indexOf(request.getKeyword())==-1) {
//    						iterator.remove();
//    					}
//    				}
//    			}
//    		}
//    		
//    		
//    		return Response.success(beeBoxes);
//    	}catch(BusinessException e) {
//    		logger.error(e.getMessage(), e);
//    		return Response.fail(e.getCode(), e.getMessage());
//    	}catch(Exception e) {
//    		logger.error("Get BeeBox Info Error",e);
//    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
//    	}
//    }
    
    @PostMapping(path = "/getPagedBeeBoxes")
    @ResponseBody
    public Response<BeeBoxPageRetrievalResponse> getPagedBeeBoxes(HttpServletRequest httpRequest, @RequestBody BeeBoxPageRetrievalRequest request) {
    	try {
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for Getting Paged BeeBox: {}====", JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		List<Long> beeFarmerIds = null;
    		List<Long> organizationIds = null;
    		Pageable page = null;
    		Page<BeeBox> beeBoxes = null;
    		BeeBoxPageRetrievalResponse response = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			return Response.success(beeBoxService.getAllBeeBoxes(request.getPageNo(), request.getPageSize()));
    		case ORGANIZATION_ADMIN:
    			organizationIds = organizationService.getIdsByAdminId(admin.getId());
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
    			if(request.getPageNo() < 1 || request.getPageSize() < 0) {
    				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    			}
    			page = PageRequest.of(request.getPageNo()-1, request.getPageSize(), Sort.Direction.ASC, "id");
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds,page);
    			response = new BeeBoxPageRetrievalResponse();
    			if(beeBoxes==null || beeBoxes.getContent()==null) {
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(0);
    			}else {
    				response.setBeeBoxes(beeBoxes.getContent());
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(beeBoxes.getTotalPages());
    			}
    			return Response.success(response);
    		case NO_ORGANIZATION_ADMIN:
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
    			if(request.getPageNo() < 1 || request.getPageSize() < 0) {
    				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    			}
    			page = PageRequest.of(request.getPageNo()-1, request.getPageSize(), Sort.Direction.ASC, "id");
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds,page);
    			response = new BeeBoxPageRetrievalResponse();
    			if(beeBoxes==null || beeBoxes.getContent()==null) {
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(0);
    			}else {
    				response.setBeeBoxes(beeBoxes.getContent());
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(beeBoxes.getTotalPages());
    			}
    			return Response.success(response);
    		}
    		return Response.fail(BizCodeEnum.NO_RIGHT.getCode(), BizCodeEnum.NO_RIGHT.getMessage());
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/deleteBeeBoxes")
    @ResponseBody
    public Response<String> deleteBeeBox(HttpServletRequest httpRequest, @RequestBody BeeBoxDeletionRequest request) {
    	try {
    		if(request==null || request.getIds()==null || request.getIds().size()==0) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for Deleting beeBox: {}====", JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		beeBoxRepository.deleteByIdIn(request.getIds());
    		return Response.success(null);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/getBeeBox")
    @ResponseBody
    public Response<BeeBox> getBeeBoxSensorData(HttpServletRequest request, @RequestBody BeeBoxRetrievalRequest beeBoxRequest){
    	try {
    		if(beeBoxRequest==null || beeBoxRequest.getBeeBoxId()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for Getting BeeBox Id： {}====",JSONObject.toJSONString(beeBoxRequest));
    		Optional<BeeBox> beeBox = beeBoxRepository.findById(beeBoxRequest.getBeeBoxId());
    		if(beeBox.isPresent()) {
    			return Response.success(beeBox.get());
    		}else {
    			return Response.success(null);
    		}
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }

    @PostMapping(path = "/getBeeBoxSensorData")
    @ResponseBody
    public Response<List<SensorData>> getBeeBoxSensorData(HttpServletRequest httpRequest, @RequestBody BeeBoxSensorDataRequest request){
    	try {
    		logger.info("====Catching the Request for Sensor Data for beeBox Id====");
    		if(request==null || request.getBeeBoxNos()==null || request.getBeeBoxNos().size()==0) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		List<BeeBox> beeBoxes = beeBoxRepository.findByBeeBoxNoIn(request.getBeeBoxNos());
    		if(beeBoxes==null || beeBoxes.size()==0) {
    			return Response.success(null);
    		}
    		List<Long> latestSensorDataIds = new LinkedList<Long>();
    		for(BeeBox beeBox: beeBoxes) {
    			latestSensorDataIds.add(beeBox.getLatestSensorDataId());
    		}
    		List<SensorData> sensorData = sensorDataRepository.findByIdIn(latestSensorDataIds);
        	return Response.success(sensorData);
    	} catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	} catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @GetMapping(path = "/getOverviewData")
    @ResponseBody
    public Response<OverviewDataResponse> getOverviewData(HttpServletRequest request) {
    	try {
    		if(request == null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for Getting beeBox: {}====");
    		Admin admin = jwtTokenUtil.getAdmin(request.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		List<Long> beeFarmerIds = null;
    		List<Long> organizationIds = null;
    		List<BeeBox> beeBoxes = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			beeBoxes = beeBoxService.getAllBeeBoxes();
    			break;
    		case ORGANIZATION_ADMIN:
    			organizationIds = organizationService.getIdsByAdminId(admin.getId());
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
    			break;
    		}
    		OverviewDataResponse response = new OverviewDataResponse();
    		if(beeBoxes==null || beeBoxes.size()==0) {
    			return Response.success(response);
    		}
    		response.setTotalBeeBoxNum(beeBoxes.size());
    		for(BeeBox beeBox: beeBoxes) {
    			if(beeBox.getProtectionStrategy()!=null && beeBox.getProtectionStrategy()) {
    				response.setProtectionNum(response.getProtectionNum()+1);
    			}
    			if(beeBox.getStatus()!=null ) {
    				if(beeBox.getStatus()==BeeBoxStatusEnum.RUNNING_STATUS.getStatus()) {
    					response.setNormalBeeBoxNum(response.getNormalBeeBoxNum()+1);
    					response.setRunningBeeBoxNum(response.getRunningBeeBoxNum()+1);
    				}else if(beeBox.getStatus()==BeeBoxStatusEnum.OFFLINE_STATUS.getStatus()) {
    					response.setOffLineBeeBoxNum(response.getOffLineBeeBoxNum()+1);
    				}else if(beeBox.getStatus()==BeeBoxStatusEnum.ABNORMAL_STATUS.getStatus()) {
    					response.setAbnormalBeeBoxNum(response.getAbnormalBeeBoxNum()+1);
    				}
    			}
    		}
    		return Response.success(response);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/getChartSensorData")
    @ResponseBody
    public Response<List<IntervalSensorData>> getChartSensorData(HttpServletRequest httpRequest, @RequestBody ChartSensoeDataRequest request) {
    	try {
    	logger.info("====Catching the Request for Getting Chart Sensor Data(token: {}), requst: {}====", httpRequest.getHeader("token"), JSONObject.toJSONString(request));
		if(request==null 
				|| request.getBeginDate()==null
				|| request.getEndDate()==null
				|| StringUtils.isEmpty(request.getBeeBoxNo())) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		return Response.success(intervalSensorDataService.getIntervalSensorData(request.getBeeBoxNo(), request.getBeginDate(), request.getEndDate()));
    	} catch(BusinessException e) {
    		logger.error("====Search Error====",e);
    		return Response.fail(e.getCode(), e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Search Error====",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/alterBeeBox")
    @ResponseBody
    public Response<String> alterBeeBox(HttpServletRequest httpRequest, @RequestBody BeeBoxAddRequest request) {
    	try {
        	logger.info("====Catching the Request for Getting Chart Sensor Data(token: {}), requst: {}====", httpRequest.getHeader("token"), JSONObject.toJSONString(request));
        	if(request==null) {
        		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        	}
        	if(request.getId()==null) {
        		BeeBox beeBox = request.getBeeBoxAdd();
        		BeeBox beeBoxDB = beeBoxRepository.findByBeeBoxNo(beeBox.getBeeBoxNo());
        		if(beeBoxDB!=null) {
        			throw new BusinessException(BizCodeEnum.BEE_BOX_NUMBER_EXISTS);
        		}
        		beeBoxRepository.save(beeBox);
        	}else {
        		Optional<BeeBox> beeBoxDB = beeBoxRepository.findById(request.getId());
        		if(beeBoxDB.isPresent()) {
        			BeeBox beeBox = request.getBeeBoxUpdate();
        			beeBox.setStatus(beeBox.getStatus());
        			beeBox.setLat(beeBox.getLat());
        			beeBox.setLng(beeBox.getLng());
        			beeBox.setEntryDate(beeBox.getEntryDate());
        			beeBox.setLatestSensorDataId(beeBox.getLatestSensorDataId());
        			beeBox.setProtectionStrategy(beeBox.getProtectionStrategy());
        			beeBox.setUpdateSensorDataTime(beeBox.getUpdateSensorDataTime());
        			beeBoxRepository.save(beeBox);
        		}else {
        			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        		}
        	}
        	return Response.success(null);
    	} catch(BusinessException e) {
    		logger.error("====Search Error====",e);
    		return Response.fail(e.getCode(), e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Search Error====",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
}
