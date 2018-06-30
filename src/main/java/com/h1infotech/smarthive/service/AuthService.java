package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.Admin;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
	Admin register(Admin admin);

    Object login(String userName, String password) throws AuthenticationException;

    String refreshToken(String token);
    
    Object updatePassword(String username, String password, String oldPassword, String mobile, String SmsCode);
}
