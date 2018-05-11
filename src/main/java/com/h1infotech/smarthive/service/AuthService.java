package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
    BeeFarmer register(BeeFarmer farmer);

    String login(String userName, String password) throws AuthenticationException;

    String logout(String token);

    String refreshToken(String token);
}
