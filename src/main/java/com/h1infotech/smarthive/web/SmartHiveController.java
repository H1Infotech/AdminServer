package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.service.SmartHiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmartHiveController {
    @Autowired
    private SmartHiveService smartHiveService;
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/partners")
    @ResponseBody
    public Response getPartners() {
        return Response.success(smartHiveService.getPartners());
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            return Response.success(authService.login(loginRequest.getName(), loginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Response register(@RequestBody BeeFarmer farmer) {
        BeeFarmer beeFarmer = smartHiveService.register(farmer);
        return Response.success(beeFarmer);
    }

    @PostMapping(path = "/logout")
    @ResponseBody
    public Response logout() {
        return null;
    }

}
