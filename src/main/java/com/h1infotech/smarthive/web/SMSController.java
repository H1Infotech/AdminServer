package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.SmsSender;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.AdminService;
import com.h1infotech.smarthive.web.request.SMSRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SMSController {
	private Logger logger = LoggerFactory.getLogger(SMSController.class);
	
	@Autowired
	SmsSender smsSender;
	
	@Autowired
	private AdminService adminService;
	
    @PostMapping(path = "/adminSMSService")
    @ResponseBody
    public Response<String> getSendMessage(HttpServletRequest httpRequest, @RequestBody SMSRequest SMSRequest){
    	logger.info("====Catching the Request for Sending SMS: {}====", JSONObject.toJSONString(SMSRequest));
    	if(SMSRequest==null 
    			|| StringUtils.isEmpty(SMSRequest.getUserName())
    			|| StringUtils.isEmpty(SMSRequest.getMessageType())
    			|| StringUtils.isEmpty(SMSRequest.getMobile())
    			|| SMSRequest.getMobile().length() != 11) {
    		return Response.fail(BizCodeEnum.ILLEGAL_INPUT);
    	}

    	try {
    		Admin admin = adminService.getAdminByUserName(SMSRequest.getUserName());
        	if(admin == null) {
        		return Response.fail(BizCodeEnum.NO_USER_INFO);
        	}
        	if(StringUtils.isEmpty(admin.getMobile())) {
        		return Response.fail(BizCodeEnum.USER_NO_MOBILE_NUM_IN_DB);
        	}
        	if(!admin.getMobile().equals(SMSRequest.getMobile())){
        		return Response.fail(BizCodeEnum.MOBILE_NUM_INCONSISTENT);
        	}
    		smsSender.dispatchSMSService(SMSRequest.getMessageType(), SMSRequest.getMobile());
    		return Response.success(null);
    	} catch(Exception e) {
    		logger.error("====Sending SMS Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }

}
