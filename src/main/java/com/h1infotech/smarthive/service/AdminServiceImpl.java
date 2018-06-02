package com.h1infotech.smarthive.service;

import org.apache.commons.lang3.StringUtils;
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	public Admin getAdminByUserName(String userName) {
		if(StringUtils.isEmpty(userName)) {
			return null;
		}
		try {
			return adminRepository.findDistinctFirstByUsername(userName);
		} catch(Exception e) {
			throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR, e);
		}
	}
}
