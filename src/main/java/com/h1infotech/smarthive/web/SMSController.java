package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.SmsSender;
import com.alibaba.fastjson.JSONObject;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.web.request.SMSRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {
	private Logger logger = LoggerFactory.getLogger(SMSController.class);
	
	@Autowired
	SmsSender smsSender;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
    @PostMapping(path = "/SMSService")
    @ResponseBody
    public Response<String> getSendMessage(HttpServletRequest httpRequest, @RequestBody SMSRequest SMSRequest){
    	logger.info("====Catching the Request for Sending SMS: {}====", JSONObject.toJSONString(SMSRequest));
    	if(SMSRequest==null 
    			|| StringUtils.isEmpty(SMSRequest.getMessageType())
    			|| StringUtils.isEmpty(SMSRequest.getMobile())
    			|| SMSRequest.getMobile().length() != 11) {
    		return Response.fail(BizCodeEnum.ILLEGAL_INPUT);
    	}
    	BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(httpRequest.getHeader("token"));
    	if(beeFarmer == null) {
    		return Response.fail(BizCodeEnum.NO_USER_INFO);
    	}
    	if(StringUtils.isEmpty(beeFarmer.getMobile())) {
    		return Response.fail(BizCodeEnum.USER_NO_MOBILE_NUM_IN_DB);
    	}
    	if(!beeFarmer.getMobile().equals(SMSRequest.getMobile())){
    		return Response.fail(BizCodeEnum.MOBILE_NUM_INCONSISTENT);
    	}
    	try {
    		smsSender.dispatchSMSService(SMSRequest.getMessageType(), SMSRequest.getMobile());
    		return Response.success(null);
    	} catch(Exception e) {
    		logger.error("====Sending SMS Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }

}
