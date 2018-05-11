package com.h1infotech.smarthive.common;

import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private BeeFarmerRepository beeFarmerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BeeFarmer user = beeFarmerRepository.findByFirstName(username);
        if (user == null)
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        return user;
    }
}
