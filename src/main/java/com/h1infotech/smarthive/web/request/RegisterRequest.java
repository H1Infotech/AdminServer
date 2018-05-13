package com.h1infotech.smarthive.web.request;

import com.h1infotech.smarthive.common.BasePOJO;
import com.h1infotech.smarthive.domain.BeeFarmer;

public class RegisterRequest extends BasePOJO {
    private String name;
    private String mobile;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BeeFarmer getBeeFarmer() {
        BeeFarmer beeFarmer = new BeeFarmer();
        beeFarmer.setName(this.getName());
        beeFarmer.setPassword(this.getPassword());
        beeFarmer.setMobile(this.getMobile());
        return beeFarmer;
    }
}
