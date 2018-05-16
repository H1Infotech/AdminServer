package com.h1infotech.smarthive.service;

import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service(value = "authService")
public class AuthServiceImpl implements AuthService {
    
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private BeeFarmerRepository beeFarmerRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public BeeFarmer register(BeeFarmer farmer) {
    	BeeFarmer registeredFarmer = beeFarmerRepository.findDistinctFirstByName(farmer.getName());
    	if (registeredFarmer != null) throw new BusinessException(BizCodeEnum.REGISTER_ERROR);
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
