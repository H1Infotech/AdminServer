package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.domain.Partner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SmartHiveController {
    @GetMapping(path = "/partners")
    @ResponseBody
    public List<Partner> getPartners() {
        return null;
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public List<Partner> login() {
        return null;
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public List<Partner> register() {
        return null;
    }

    @PostMapping(path = "/logout")
    @ResponseBody
    public List<Partner> logout() {
        return null;
    }

}
