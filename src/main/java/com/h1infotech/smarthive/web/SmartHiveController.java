package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.service.SmartHiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class SmartHiveController {
    @Autowired
    private SmartHiveService smartHiveService;

    @GetMapping(path = "/partners")
    @ResponseBody
    public Response getPartners() {
        return Response.success(smartHiveService.getPartners());
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public List<Partner> login() {
        return null;
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Response register(@RequestBody BeeFarmer farmer) {
        smartHiveService.register(farmer);
        return null;
    }

    @PostMapping(path = "/logout")
    @ResponseBody
    public List<Partner> logout() {
        return null;
    }

}
