package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "authService")
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BeeFarmerRepository beeFarmerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BeeFarmer register(BeeFarmer farmer) {
        BeeFarmer registeredFarmer = beeFarmerRepository.findDistinctFirstByName(farmer.getName());
        if (registeredFarmer != null) throw new IllegalArgumentException("user.exists.error");
        farmer.setPassword(bCryptPasswordEncoder.encode(farmer.getPassword()));
        return beeFarmerRepository.save(farmer);
    }

    @Override
    public Object login(String userName, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(userName);
        ((BeeFarmer) authentication.getPrincipal()).setAuthToken(token);
        stringRedisTemplate.opsForValue().set(token, "true");
        return authentication.getPrincipal();
    }

    @Override
    public String refreshToken(String token) {
        return null;
    }
}
