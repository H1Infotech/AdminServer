package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import java.util.Optional;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.Event;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.service.EventService;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.repository.EventRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.web.request.SingleEventRequest;
import com.h1infotech.smarthive.web.request.EventDeletionRequest;
import com.h1infotech.smarthive.web.request.AlertRuleAlerationRequest;

@RestController
@RequestMapping("/api")
public class EventController {
	
	private Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    EventService eventService;
    
    @Autowired
    EventRepository eventRepository;
    
    @Autowired
    AdminRepository adminRepository;
    
    @GetMapping(path = "/getEvents")
    @ResponseBody
	public Response<List<Event>> getEvents(HttpServletRequest httpRequest) {
    	try {
    		logger.info("====Catching the Request for Getting Events: {}====");
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    			case SUPER_ADMIN:
    			case SENIOR_ADMIN:
    				return Response.success(eventService.getAllEvents());
    			case ORGANIZATION_ADMIN:
    				List<Long> adminIds = new LinkedList<Long>();
    				adminIds.add(admin.getId());
    				return Response.success(eventService.getEventByAdminIdIn(adminIds));
    			case NO_ORGANIZATION_ADMIN:
    				return Response.success(eventService.getNoOrganizationEvents());
    		}
    		return Response.fail(BizCodeEnum.NO_RIGHT);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/getEvent")
    @ResponseBody
	public Response<Event> getEvents(HttpServletRequest httpRequest, @RequestBody SingleEventRequest request) {
    	try {
    		logger.info("====Catching the Request for Getting Event: {}====", JSONObject.toJSON(request));
    		if(request==null
    				|| request.getId()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		
    		Optional<Event> event = eventRepository.findById(request.getId());
    		if(event.isPresent()) {
    			return Response.success(event.get());
    		}
    		return Response.fail(BizCodeEnum.NO_SUCH_EVENT);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/deleteEvents")
    @ResponseBody
	public Response<String> deleteEvents(HttpServletRequest httpRequest, @RequestBody EventDeletionRequest request) {
    	try {
    		logger.info("====Catching the Request for Deleting Events: {}====",JSONObject.toJSONString(request));
    		if(request==null
    				|| request.getIds()==null
    				|| request.getIds().size()==0) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		eventRepository.deleteByIdIn(request.getIds());
    		return Response.success(null);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/alterAlertRule")
    @ResponseBody
	public Response<String> alterAlertRule(HttpServletRequest httpRequest, @RequestBody AlertRuleAlerationRequest request) {
    	try {
    		logger.info("====Catching the Request for Add & Update Event: {}====",JSONObject.toJSONString(request));
    		if(request==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		Event event = request.getEvent();
    		//1:溫度，2：濕度，3壓強，4電量,5重力
    		String action = null;
    		String condition = null;
    		if(event.getRuleType()==1) {
    			condition="temperature";
    		}else if(event.getRuleType()==2) {
    			condition="humidity";
    		}else if(event.getRuleType()==3) {
    			condition="airPressure";
    		}else if(event.getRuleType()==4) {
    			condition="battery";
    		}else if(event.getRuleType()==5) {
    			condition="gravity";
    		}else {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		if(event.getMinThreshold()!=null && event.getMaxThreshold()!=null) {
    			action = String.format("#o.%s > %f && #o.%s < %f", condition, event.getMinThreshold(), condition, event.getMaxThreshold());
    		}else if(event.getMinThreshold()!=null) {
    			action = String.format("#o.%s > %f", condition, event.getMinThreshold());
    		}else if(event.getMaxThreshold()!=null) {
    			action = String.format("#o.%s < %f", condition, event.getMaxThreshold());
    		}
    		if(StringUtils.isEmpty(action)){
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		event.setAction(action);
    		eventRepository.save(event);
    		return Response.success(null);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
}
