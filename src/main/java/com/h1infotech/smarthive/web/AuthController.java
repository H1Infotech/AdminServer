package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.service.PartnerService;
import com.h1infotech.smarthive.web.request.LoginRequest;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.web.request.RegisterRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.web.request.PasswordUpteRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    PartnerService partnerService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/login")
    @ResponseBody
    public Response<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
        	logger.info("====Catching the Request for Login: " + JSONObject.toJSONString(loginRequest) + "====");
        	Object response = authService.login(loginRequest.getUserName(), loginRequest.getPassword());
        	logger.info("====Login Response: " + JSONObject.toJSONString(response) + "====");
            return Response.success(response);
        } catch (BusinessException e) {
        	logger.error(e.getMessage(), e);
            return Response.fail(e.getCode(), e.getMessage());
        } catch(Exception e) {
        	logger.error("Login Error", e);
        	return Response.fail(BizCodeEnum.LOGIN_ERROR);
        }
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Response<Object> register(@RequestBody RegisterRequest registerRequest) {
        try {
        	logger.info("====Catching the Request for Register: " + JSONObject.toJSONString(registerRequest) + "====");
            BeeFarmer beeFarmer = authService.register(registerRequest.getBeeFarmer());
            logger.info("====Register Response: " + JSONObject.toJSONString(beeFarmer) + "====");
            return Response.success(beeFarmer);
        } catch (BusinessException e) {
        	logger.error(e.getMessage(), e);
            return Response.fail(e.getCode(), e.getMessage());
        } catch(Exception e) {
        	logger.error("Register Error", e);
        	return Response.fail(BizCodeEnum.REGISTER_ERROR);
        }
    }
    
    @PostMapping(path = "/updatePassword")
    @ResponseBody
    public Response<Object> updatePassword(HttpServletRequest httpRequest, @RequestBody PasswordUpteRequest request) {
    	try {
    		logger.info("====Catching the Request for Updating Password( token: " + httpRequest.getHeader("token")+", "+JSONObject.toJSONString(request) + "====");
    		String userName = jwtTokenUtil.getUsernameFromToken(httpRequest.getHeader("token"));
    		String password = bCryptPasswordEncoder.encode(request.getPassword());
    		boolean firstTime = false;
    		return Response.success(authService.updatePassword(userName, password, firstTime));
    	} catch(BusinessException e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/getBeeFarmerEntity")
    @ResponseBody
    public Response<Object> getBeeFarmerEntity(HttpServletRequest httpRequest) {
    	try {
    		logger.info("====Catching the Request for Getting Bee Farmer( token: " + httpRequest.getHeader("token") + "====");
    		BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(httpRequest.getHeader("token"));
    		if(beeFarmer.getPartnerId()!=null) {
    			Partner partner = partnerService.getParterById(beeFarmer.getPartnerId());
    			beeFarmer.setPartnerName(partner.getContactName());
    		}
    		return Response.success(beeFarmer);
    	} catch(BusinessException e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
}
