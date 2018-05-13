package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.service.AuthService;
import com.h1infotech.smarthive.service.SmartHiveService;
import com.h1infotech.smarthive.web.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmartHiveController {
    @Autowired
    private SmartHiveService smartHiveService;

    @GetMapping(path = "/partners")
    @ResponseBody
    public Response getPartners() {
        return Response.success(smartHiveService.getPartners());
    }


}
