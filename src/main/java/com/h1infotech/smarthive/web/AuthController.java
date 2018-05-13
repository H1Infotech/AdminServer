package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.web.request.LoginRequest;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h1infotech.smarthive.web.request.RegisterRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    @ResponseBody
    public Response<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
        	logger.info("====Catching the Request for Login: " + loginRequest + "====");
            return Response.success(authService.login(loginRequest.getUserName(), loginRequest.getPassword()));
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
        	logger.info("====Catching the Request for Register: " + registerRequest + "====");
            BeeFarmer beeFarmer = authService.register(registerRequest.getBeeFarmer());
            return Response.success(beeFarmer);
        } catch (BusinessException e) {
        	logger.error(e.getMessage(), e);
            return Response.fail(e.getCode(), e.getMessage());
        } catch(Exception e) {
        	logger.error("Register Error", e);
        	return Response.fail(BizCodeEnum.REGISTER_ERROR);
        }
    }
}
