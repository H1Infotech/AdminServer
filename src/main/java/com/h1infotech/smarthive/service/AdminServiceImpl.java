package com.h1infotech.smarthive.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.h1infotech.smarthive.domain.Admin;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.common.BizCodeEnum;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.AdminRightRepository;
import org.springframework.transaction.annotation.Transactional;
import com.h1infotech.smarthive.web.request.AdminDeletionRequest;
import com.h1infotech.smarthive.repository.BeeBoxGroupRepository;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;
import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	BeeFarmerService beeFarmerService;
	
    @Autowired
    private AdminRightRepository adminRightRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	private BeeBoxGroupRepository beeBoxGroupRepository;
	
	@Autowired
	private BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;
	
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
		if(pageAdmin!=null && pageAdmin.getContent()!=null && pageAdmin.getContent().size()>0) {
			List<Long> organizationIds = new LinkedList<Long>();
			for(Admin admin: pageAdmin.getContent()) {
				if(admin.getOrganizationId()!=null) {
					organizationIds.add(admin.getOrganizationId());
				}
			}
			if(organizationIds!=null && organizationIds.size()>0) {
				List<Organization> organizations = organizationRepository.findByIdIn(organizationIds);
				Map<Long, Organization> map = new HashMap<Long, Organization>();
				if(organizations!=null && organizations.size()>0) {
					for(Organization organization: organizations) {
						map.put(organization.getId(), organization);
					}
				}
				for(Admin admin: pageAdmin.getContent()) {
					Organization organization = map.get(admin.getOrganizationId());
					if(organization!=null && !StringUtils.isEmpty(organization.getOrganizationName())) {
						admin.setOrganizationName(organization.getOrganizationName());
					}
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
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		List<Admin> admins = adminRepository.findByTypeNotAndIdNot(AdminTypeEnum.SUPER_ADMIN.getType(), excludeId, sort);
		if(admins!=null && admins.size()>0) {
			List<Long> organizationIds = new LinkedList<Long>();
			for(Admin admin: admins) {
				if(admin.getOrganizationId()!=null) {
					organizationIds.add(admin.getOrganizationId());
				}
			}
			if(organizationIds!=null && organizationIds.size()>0) {
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
		}
		return admins;
	}
	
	@Override
	public List<Admin> getAllOrganizationAdmins() {
		List<Integer> types = new LinkedList<Integer>();
		types.add(3);
		types.add(4);
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return adminRepository.findByTypeIn(types,sort);
	}

	@Override
	@Transactional
	public Response<String> deleteAdmins(HttpServletRequest httpRequest, AdminDeletionRequest request) {
		if(request==null || request.getAdminIds()==null || request.getAdminIds().size()==0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		adminRepository.deleteByIdIn(request.getAdminIds());
		List<Organization> organizations = organizationRepository.deleteByAdminIdIn(request.getAdminIds());
		if(organizations!=null && organizations.size()>0) {
			List<Long> organizationIds = new LinkedList<Long>();
			for(Organization organization: organizations) {
				organizationIds.add(organization.getId());
			}
			beeFarmerService.wipeOutOrganizationId(organizationIds);
		}
		adminRightRepository.deleteByAdminIdIn(request.getAdminIds());
		List<BeeBoxGroup> beeBoxGroups = beeBoxGroupRepository.deleteByAdminIdIn(request.getAdminIds());
		if(beeBoxGroups==null||beeBoxGroups.size()==0) {
			return Response.success(null);
		}
		List<Long> beeBoxGroupIds = new LinkedList<Long>();
		for(BeeBoxGroup beeBoxGroup: beeBoxGroups) {
			beeBoxGroupIds.add(beeBoxGroup.getId());
		}
		if(beeBoxGroupIds!=null && beeBoxGroupIds.size()>0) {
			beeBoxGroupAssociationRepository.deleteByGroupIdIn(beeBoxGroupIds);
		}
		return Response.success(null);
	}
}
