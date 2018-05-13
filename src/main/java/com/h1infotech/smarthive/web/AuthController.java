package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.web.request.LoginRequest;
import com.h1infotech.smarthive.web.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    @ResponseBody
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            return Response.success(authService.login(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("login.password.error");
        }
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Response register(@RequestBody RegisterRequest registerRequest) {
        try {
            BeeFarmer beeFarmer = authService.register(registerRequest.getBeeFarmer());
            return Response.success(beeFarmer);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
