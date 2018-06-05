package com.h1infotech.smarthive.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.common.BizCodeEnum;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	@Override
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
	
	@Override
	public AdminPageRetrievalResponse getAdmins(long excludeId, int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<Admin> pageAdmin = adminRepository.findByTypeNotAndIdNot(AdminTypeEnum.SUPER_ADMIN.getType(), excludeId, page);
		AdminPageRetrievalResponse response = new AdminPageRetrievalResponse();
		if(pageAdmin.getContent()!=null) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(pageAdmin.getTotalPages());
			response.setAdmins(pageAdmin.getContent());
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}
	
	@Override
	public List<Admin> getAllOrganizationAdmins() {
		List<Integer> types = new LinkedList<Integer>();
		types.add(3);
		types.add(4);
		return adminRepository.findByTypeIn(types);
	}

}
