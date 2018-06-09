package com.h1infotech.smarthive.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.common.BizCodeEnum;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
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
		PageRequest page = PageRequest.of(pageNo-1, pageSize, Sort.Direction.ASC, "id");
		Page<Admin> pageAdmin = adminRepository.findByTypeNotAndIdNot(AdminTypeEnum.SUPER_ADMIN.getType(), excludeId, page);
		AdminPageRetrievalResponse response = new AdminPageRetrievalResponse();
		if(pageAdmin.getContent()!=null && pageAdmin.getContent().size()>0) {
			List<Long> organizationIds = new LinkedList<Long>();
			for(Admin admin: pageAdmin.getContent()) {
				organizationIds.add(admin.getOrganizationId());
			}
			List<Organization> organizations = organizationRepository.findByIdIn(organizationIds);
			Map<Long, Organization> map = new HashMap<Long, Organization>();
			if(organizations!=null && organizations.size()>0) {
				for(Organization organization: organizations) {
					map.put(organization.getId(), organization);
				}
			}
			for(Admin admin: pageAdmin.getContent()) {
				Organization organization = map.get(admin.getOrganizationId());
				if(organization!=null && StringUtils.isEmpty(organization.getOrganizationName())) {
					admin.setOrganizationName(organization.getOrganizationName());
				}
			}
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
	public List<Admin> getAdmins(long excludeId) {
		List<Admin> admins = adminRepository.findByTypeNotAndIdNot(AdminTypeEnum.SUPER_ADMIN.getType(), excludeId);
		if(admins!=null && admins.size()>0) {
			List<Long> organizationIds = new LinkedList<Long>();
			for(Admin admin: admins) {
				organizationIds.add(admin.getOrganizationId());
			}
			List<Organization> organizations = organizationRepository.findByIdIn(organizationIds);
			Map<Long, Organization> map = new HashMap<Long, Organization>();
			if(organizations!=null && organizations.size()>0) {
				for(Organization organization: organizations) {
					map.put(organization.getId(), organization);
				}
			}
			for(Admin admin: admins) {
				Organization organization = map.get(admin.getOrganizationId());
				if(organization!=null && StringUtils.isEmpty(organization.getOrganizationName())) {
					admin.setOrganizationName(organization.getOrganizationName());
				}
			}
		}
		return admins;
	}
	
	@Override
	public List<Admin> getAllOrganizationAdmins() {
		List<Integer> types = new LinkedList<Integer>();
		types.add(3);
		types.add(4);
		return adminRepository.findByTypeIn(types);
	}

}
