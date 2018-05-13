package com.h1infotech.smarthive.web;

import com.h1infotech.smarthive.common.BasePOJO;

public class LoginRequest extends BasePOJO {
    
    private String mobile;
    private String certCode;
    private String password;
    private String userName;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String name) {
        this.userName = name;
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
    public String getCertCode() {
        return certCode;
    }
    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }
}
