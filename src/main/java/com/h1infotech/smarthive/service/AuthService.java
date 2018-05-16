package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
    BeeFarmer register(BeeFarmer farmer);

    Object login(String userName, String password) throws AuthenticationException;

    String refreshToken(String token);
    
    Object updatePassword(String userName, String password, boolean firstTime);
}
