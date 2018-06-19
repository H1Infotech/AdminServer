package com.h1infotech.smarthive.web;

import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Optional;
import java.util.Iterator;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.util.Collections;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Sort;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;
import com.h1infotech.smarthive.common.Response;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.domain.BeeFarmer;
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

import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;
import com.h1infotech.smarthive.repository.BeeBoxGroupRepository;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.web.request.BeeBoxAddRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.service.IntervalSensorDataService;
import com.h1infotech.smarthive.web.response.OverviewDataResponse;
import com.h1infotech.smarthive.web.request.BeeBoxDeletionRequest;
import com.h1infotech.smarthive.web.request.BeeBoxRetrievalRequest;
import com.h1infotech.smarthive.web.request.ChartSensoeDataRequest;
import com.h1infotech.smarthive.web.request.AmbiguousSearchRequest;
import com.h1infotech.smarthive.web.request.BeeBoxSensorDataRequest;
import com.h1infotech.smarthive.web.request.BeeBoxPageRetrievalRequest;
import com.h1infotech.smarthive.repository.HistoryAlertEventRepository;
import com.h1infotech.smarthive.repository.IntervalSensorDataRepository;
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
    HistoryAlertEventRepository historyAlertEventRepository;
    
    @Autowired
    BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;
    
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Autowired
	IntervalSensorDataService intervalSensorDataService;
	
	@Autowired
	IntervalSensorDataRepository intervalSensorDataRepository;
	
	@Autowired
	BeeBoxGroupRepository beeBoxGroupRepository;
	
	@PostMapping(path = "/getAllBeeBoxSensorData")
	@ResponseBody
	public Response<List<SensorData>> getAllBeeBoxSensorData(HttpServletRequest httpRequest, @RequestBody AmbiguousSearchRequest request) {
		try {
			logger.info("====Catching the Request for Getting BeeBox Sensor Data. Ambiguous Search Request: {}====", JSONObject.toJSONString(request));
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
					if(organizationIds!=null && organizationIds.size()>0) {
						beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
						if(beeFarmerIds!=null && beeFarmerIds.size()>0) {
							beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
						}
					}
					break;
				case NO_ORGANIZATION_ADMIN:
					beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
					if(beeFarmerIds!=null && beeFarmerIds.size()>0) {
						beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
					}
					break;
			}
			if (beeBoxes == null || beeBoxes.size() == 0) {
				return Response.success(null);
			}
			
    		if(!StringUtils.isEmpty(request.getKeyword())) {
   				Iterator<BeeBox> iterator = beeBoxes.iterator();
   				while(iterator.hasNext()) {
    				if(iterator.next().getDesc().indexOf(request.getKeyword())==-1) {
    					iterator.remove();
    				}
    			}
    		}
			
			List<Long> ids = new LinkedList<Long>();
			List<SensorData> sensorData = new LinkedList<SensorData>();
			for (BeeBox beeBox : beeBoxes) {
				if (beeBox.getLatestSensorDataId() == null) {
					SensorData data = new SensorData();
					data.setId(beeBox.getId());
					data.setBeeBoxNo(beeBox.getBeeBoxNo());
					data.setStatus(3);
					sensorData.add(data);
				} else {
					ids.add(beeBox.getLatestSensorDataId());
				}
			}
			List<SensorData> existSensorData = sensorDataService.getSensorDara(ids);
			
			if(existSensorData!=null) {
				sensorData.addAll(existSensorData);
			}
			Collections.sort(sensorData);
			return Response.success(sensorData);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("getAllBeeBoxSensorData", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}  
    
    @PostMapping(path = "/getBeeBoxByPosition")
    @ResponseBody
    public Response<BeeBox> getBeeBoxes(HttpServletRequest request, @RequestBody Map<String, String> position) {
    	try {
    		logger.info("====Catching the Request for Getting Bee Farmer BeeBox By Position: {}====", JSONObject.toJSONString(position));
    		if(position==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		String latStr = position.get("lat");
    		String lngStr = position.get("lng");
    		if(StringUtils.isEmpty(latStr) || StringUtils.isEmpty(lngStr)) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		BigDecimal lat = new BigDecimal(latStr);
    		BigDecimal lng = new BigDecimal(lngStr);
    		BeeBox beeBox = beeBoxRepository.findByLatAndLng(lat, lng);
    		if(beeBox == null) {
    			throw new BusinessException(BizCodeEnum.NO_BEE_BOX_INFO);
    		}
    		return Response.success(beeBox);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("getBeeBoxByPosition",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
	
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
    			if(organizationIds==null || organizationIds.size()==0) {
        			response = new BeeBoxPageRetrievalResponse();
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(0);
    				return Response.success(response);
    			}
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
    			if(beeFarmerIds==null || beeFarmerIds.size()==0) {
        			response = new BeeBoxPageRetrievalResponse();
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(0);
    				return Response.success(response);
    			}
    			if(request.getPageNo() < 1 || request.getPageSize() < 0) {
    				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    			}
    			page = PageRequest.of(request.getPageNo()-1, request.getPageSize(), Sort.Direction.ASC, "id");
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds,page);
    			response = new BeeBoxPageRetrievalResponse();
    			if(beeBoxes==null || beeBoxes.getContent()==null || beeBoxes.getContent().size()==0) {
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
    			if(beeFarmerIds==null || beeFarmerIds.size()==0) {
        			response = new BeeBoxPageRetrievalResponse();
    				response.setCurrentPageNo(request.getPageNo());
    				response.setTotalPageNo(0);
    				return Response.success(response);
    			}
    			if(request.getPageNo() < 1 || request.getPageSize() < 0) {
    				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    			}
    			page = PageRequest.of(request.getPageNo()-1, request.getPageSize(), Sort.Direction.ASC, "id");
    			beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds,page);
    			response = new BeeBoxPageRetrievalResponse();
    			if(beeBoxes==null || beeBoxes.getContent()==null || beeBoxes.getContent().size()==0) {
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
    		logger.error("getPagedBeeBoxes",e);
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
    		List<BeeBox> beeBoxes = beeBoxRepository.deleteByIdIn(request.getIds());
    		historyAlertEventRepository.deleteByBeeBoxIdIn(request.getIds());
    		List<BeeBoxGroupAssociation> associations = beeBoxGroupAssociationRepository.deleteByBeeBoxIdIn(request.getIds());
    		if(associations!=null && associations.size()>0) {
    			Map<Long, Integer> groupBeeBoxNumMap = new HashMap<Long, Integer>();
    			for(BeeBoxGroupAssociation association: associations) {
    				if(association!=null && association.getGroupId()!=null) {
    					Integer num = groupBeeBoxNumMap.get(association.getGroupId());
        				num = num==null?1:num+1;
        				groupBeeBoxNumMap.put(association.getGroupId(), num);
    				}
    			}
    			
    			if(groupBeeBoxNumMap.keySet()!=null && groupBeeBoxNumMap.keySet().size()>0) {
        			for(Long groupId: groupBeeBoxNumMap.keySet()) {
        				Optional<BeeBoxGroup> group = beeBoxGroupRepository.findById(groupId);
        				if(group.isPresent()) {
        					Integer currentNum = group.get().getBeeBoxNum() - groupBeeBoxNumMap.get(groupId);
        					currentNum = currentNum<0?0:currentNum;
        					if(currentNum>0) {
        						group.get().setBeeBoxNum(currentNum);
        						beeBoxGroupRepository.save(group.get());
        					}else {
        						beeBoxGroupRepository.deleteById(group.get().getId());
        					}
        				}
        			}
    			}
    		}
    		if(beeBoxes==null || beeBoxes.size()==0) {
    			return Response.success(null);
    		}
    		List<String> beeBoxNos = new LinkedList<String>();
    		Map<Long, Integer> beeBoxNumMap = new HashMap<Long, Integer>();
    		for(BeeBox beeBox: beeBoxes) {
    			if(beeBox!=null && !StringUtils.isEmpty(beeBox.getBeeBoxNo())) {
    				beeBoxNos.add(beeBox.getBeeBoxNo());
    			}
    			if(beeBox!=null && beeBox.getFarmerId()!=null) {
    				Integer num = beeBoxNumMap.get(beeBox.getFarmerId());
    				num = num==null?1:num+1;
    				beeBoxNumMap.put(beeBox.getFarmerId(), num);
    			}
    		}
    		if(beeBoxNos.size()>0) {
    			intervalSensorDataRepository.deleteByBeeBoxNoIn(beeBoxNos);
        		sensorDataRepository.deleteByBeeBoxNoIn(beeBoxNos);
    		}
    		if(beeBoxNumMap.keySet()!=null && beeBoxNumMap.keySet().size()>0) {
    			for(Long farmerId: beeBoxNumMap.keySet()) {
    				Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(farmerId);
    				if(beeFarmer.isPresent()) {
    					Integer currentNum = beeFarmer.get().getBeeBoxNum() - beeBoxNumMap.get(farmerId);
    					currentNum = currentNum<0?0:currentNum;
    					beeFarmer.get().setBeeBoxNum(currentNum);
    					beeFarmerRepository.save(beeFarmer.get());
    				}
    			}
    		}
			return Response.success(null);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("deleteBeeBoxes",e);
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
    		logger.error("getBeeBox",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }

    @PostMapping(path = "/getBeeBoxSensorData")
    @ResponseBody
    public Response<List<SensorData>> getBeeBoxSensorData(HttpServletRequest httpRequest, @RequestBody BeeBoxSensorDataRequest request){
    	try {
    		logger.info("====Catching the Request for Sensor Data for beeBox Id====", JSONObject.toJSONString(request));
    		if(request==null || request.getBeeBoxNos()==null || request.getBeeBoxNos().size()==0 || request.getSensorDataTag()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		List<BeeBox> beeBoxes = beeBoxRepository.findByBeeBoxNoIn(request.getBeeBoxNos());
    		if(beeBoxes==null || beeBoxes.size()==0) {
    			return Response.success(null);
    		}
    		List<SensorData> sensorData = new LinkedList<SensorData>();
    		List<Long> latestSensorDataIds = new LinkedList<Long>();
    		for(BeeBox beeBox: beeBoxes) {
    			Long latestSensorDataId = beeBox.getLatestSensorDataId();
    			
    			if(latestSensorDataId==null) {
    				SensorData data = new SensorData();
    				data.setBeeBoxNo(beeBox.getBeeBoxNo());
    				data.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
    				sensorData.add(data);
    			}else if(request.getSensorDataTag().get(beeBox.getBeeBoxNo())==null || beeBox.getLatestSensorDataId()>(request.getSensorDataTag().get(beeBox.getBeeBoxNo()))) {
    				latestSensorDataIds.add(beeBox.getLatestSensorDataId());
    			}
    		}
    		
    		if(latestSensorDataIds.size()>0) {
    			List<SensorData> newSensorData = sensorDataRepository.findByIdIn(latestSensorDataIds);
    			sensorData.addAll(newSensorData);
    		}
    		Collections.sort(sensorData);
        	return Response.success(sensorData);
    	} catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	} catch(Exception e) {
    		logger.error("getBeeBoxSensorData",e);
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
    		logger.info("====Catching the Request for Getting beeBox====");
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
    			if(organizationIds!=null && organizationIds.size()>0) {
    				beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
    				if(beeFarmerIds!=null && beeFarmerIds.size()>0) {
    					beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
    				}
    			}
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
    			if(beeFarmerIds!=null && beeFarmerIds.size()>0) {
    				beeBoxes = beeBoxRepository.findByFarmerIdIn(beeFarmerIds);
    			}
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
    				if(beeBox.getStatus()==BeeBoxStatusEnum.RUNNING_STATUS.getStatus()|| beeBox.getStatus()==BeeBoxStatusEnum.NORMAL_STATUS.getStatus()) {
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
    		logger.error("getOverviewData",e);
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
    		logger.error("====getChartSensorData====",e);
    		return Response.fail(e.getCode(), e.getMessage());
    	} catch(Exception e) {
    		logger.error("====getChartSensorData====",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/alterBeeBox")
    @ResponseBody
    public Response<String> alterBeeBox(HttpServletRequest httpRequest, @RequestBody BeeBoxAddRequest request) {
    	try {
        	logger.info("====Catching the Request for Getting Chart Sensor Data(token: {}), requst: {}====", httpRequest.getHeader("token"), JSONObject.toJSONString(request));
        	if(request==null || request.getFarmerId()==null) {
        		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        	}
        	if(request.getId()==null) {
        		Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(request.getFarmerId());
        		if(beeFarmer==null || !beeFarmer.isPresent()) {
        			throw new BusinessException(BizCodeEnum.NO_FARMER_INFO);
        		}
        		BeeBox beeBox = request.getBeeBoxAdd();
        		BeeBox beeBoxDB = beeBoxRepository.findByBeeBoxNo(beeBox.getBeeBoxNo());
        		if(beeBoxDB!=null) {
        			throw new BusinessException(BizCodeEnum.BEE_BOX_NUMBER_EXISTS);
        		}
        		beeBoxRepository.save(beeBox);
        		int beeBoxNum = beeFarmer.get().getBeeBoxNum()==null?0:beeFarmer.get().getBeeBoxNum();
        		beeFarmer.get().setBeeBoxNum(beeBoxNum+1);
        		beeFarmerRepository.save(beeFarmer.get());
        	}else {
        		Optional<BeeBox> beeBoxDB = beeBoxRepository.findById(request.getId());
        		if(beeBoxDB.isPresent()) {
        			BeeBox beeBox = request.getBeeBoxUpdate();
        			beeBox.setStatus(beeBox.getStatus());
        			beeBox.setLat(beeBox.getLat());
        			beeBox.setLng(beeBox.getLng());
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
