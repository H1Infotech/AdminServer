package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mysql.jdbc.StringUtils;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import org.springframework.web.bind.annotation.*;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.service.SmartHiveService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SmartHiveController {
    
	private final Logger logger = LoggerFactory.getLogger(SmartHiveController.class);
	
	@Autowired
    private AuthService authService;
	
	@Autowired
    private SmartHiveService smartHiveService;
	
    @GetMapping(path = "/partners")
    @ResponseBody
    public Response<List<Partner>> getPartners() {
        return Response.success(smartHiveService.getPartners());
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public Response<String> login(@RequestBody LoginRequest loginRequest) {
    	
        try {
        	logger.info("====Catching the Login Request: " + loginRequest + "====");
        	
        	// 0. Validate the Request
        	if(loginRequest == null || StringUtils.isEmptyOrWhitespaceOnly(loginRequest.getUserName()) || StringUtils.isEmptyOrWhitespaceOnly(loginRequest.getPassword())) {
        		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        	}
        	// 1. Authenticate User's Information
            return Response.success(authService.login(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (BusinessException e) {
        	logger.error("Loging Error", e);
            return Response.fail(e.getCode(), e.getMessage());
        } catch(Exception e) {
        	logger.error("Loging Error", e);
        	return Response.fail(BizCodeEnum.SERVICE_ERROR);
        }
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Response<BeeFarmer> register(@RequestBody BeeFarmer farmer) {
        BeeFarmer beeFarmer = smartHiveService.register(farmer);
        return Response.success(beeFarmer);
    }

    @PostMapping(path = "/logout")
    @ResponseBody
    public Response<String> logout() {
        return null;
    }
    
//    @PostMapping(path = "/logout")
//    @ResponseBody
//    public Response<>

}
