package com.h1infotech.smarthive.web.request;

public class LoginRequest {
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
}
