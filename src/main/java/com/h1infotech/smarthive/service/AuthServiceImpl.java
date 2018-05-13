package com.h1infotech.smarthive.service;

import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service(value = "authService")
public class AuthServiceImpl implements AuthService {
   
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public BeeFarmer register(BeeFarmer farmer) {
        return null;
    }

    @Override
    public String login(String userName, String password) throws AuthenticationException {
    	try {
    		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
    		final Authentication authentication = authenticationManager.authenticate(upToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
    		return jwtTokenUtil.generateToken(userName);
    	}catch(Exception e) {
    		throw new BusinessException(BizCodeEnum.LOGIN_ERROR, e);
    	}
    }

    @Override
    public String logout(String token) {
        return null;
    }

    @Override
    public String refreshToken(String token) {
        return null;
    }
}
