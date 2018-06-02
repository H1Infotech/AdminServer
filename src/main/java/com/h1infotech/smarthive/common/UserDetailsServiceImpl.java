package com.h1infotech.smarthive.common;

import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service(value = "userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Admin user = adminRepository.findDistinctFirstByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        return user;
    }
}
