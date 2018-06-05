package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.web.request.LoginRequest;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.web.request.PasswordUpteRequest;

@RestController
@RequestMapping("/api")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
        
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/adminLogin")
    @ResponseBody
    public Response<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
        	logger.info("====Catching the Request for Login: " + JSONObject.toJSONString(loginRequest) + "====");
        	if(loginRequest==null 
        			|| StringUtils.isEmpty(loginRequest.getUserName())
        			|| StringUtils.isEmpty(loginRequest.getPassword())) {
        		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        	}
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
    
    @PostMapping(path = "/adminUpdatePassword")
    @ResponseBody
    public Response<Object> updatePassword(HttpServletRequest httpRequest, @RequestBody PasswordUpteRequest request) {
    	try {
    		logger.info("====Catching the Request for Updating Password: {}====", JSONObject.toJSONString(request));
    		if(request==null || StringUtils.isEmpty(request.getSmsCode()) 
        			|| StringUtils.isEmpty(request.getPassword())
        			|| StringUtils.isEmpty(request.getMobile())
        			|| StringUtils.isEmpty(request.getUsername())) {
        		throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
        	}
    		return Response.success(authService.updatePassword(request.getUsername(), request.getPassword(), request.getMobile(), request.getSmsCode()));
    	} catch(BusinessException e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
}
