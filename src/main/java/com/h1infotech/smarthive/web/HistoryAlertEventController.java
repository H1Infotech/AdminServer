package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.h1infotech.smarthive.domain.Admin;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.domain.HistoryAlertEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.web.request.AmbiguousSearchRequest;
import com.h1infotech.smarthive.repository.HistoryAlertEventRepository;
@RestController
@RequestMapping("/api")
public class HistoryAlertEventController {
	
	private Logger logger = LoggerFactory.getLogger(EventController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    AdminRepository adminRepository;
    
    @Autowired
    HistoryAlertEventRepository historyAlertEventRepository;

    @GetMapping(path = "/getHistoryAlertEvents")
    @ResponseBody
	public Response<List<HistoryAlertEvent>> getHistoryAlertEvents(HttpServletRequest httpRequest) {
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
				return Response.success(historyAlertEventRepository.findAll());
			case ORGANIZATION_ADMIN:
				return Response.success(historyAlertEventRepository.findByAdminId(admin.getId()));
			case NO_ORGANIZATION_ADMIN:
				List<Integer> types = new LinkedList<Integer>();
				types.add(4);
				List<Admin> admins = adminRepository.findByTypeIn(types);
				List<Long> ids = new LinkedList<Long>();
				for(Admin one: admins) {
					ids.add(one.getId());
				}
				return Response.success(historyAlertEventRepository.findByAdminIdIn(ids));
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
    
    @PostMapping(path = "/searchHistoryAlertEvents")
    @ResponseBody
	public Response<List<HistoryAlertEvent>> searchHistoryAlertEvents(HttpServletRequest httpRequest, @RequestBody AmbiguousSearchRequest request) {
    	try {
    		logger.info("====Catching the Request for Getting Events: {}====");
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		List<HistoryAlertEvent> events = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
			case SUPER_ADMIN:
			case SENIOR_ADMIN:
				events = historyAlertEventRepository.findAll();
			case ORGANIZATION_ADMIN:
				events = historyAlertEventRepository.findByAdminId(admin.getId());
			case NO_ORGANIZATION_ADMIN:
				List<Integer> types = new LinkedList<Integer>();
				types.add(4);
				List<Admin> admins = adminRepository.findByTypeIn(types);
				List<Long> ids = new LinkedList<Long>();
				for(Admin one: admins) {
					ids.add(one.getId());
				}
				events = historyAlertEventRepository.findByAdminIdIn(ids);
    		}
    		
    		if(events==null || events.size()==0) {
    			Iterator<HistoryAlertEvent> iterator = events.iterator();
    			while(iterator.hasNext()) {
    				if(iterator.next().getDesc().indexOf(request.getKeyword())==-1) {
    					iterator.remove();
    				}
    			}
    		}
    		
    		return Response.success(events);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
}
