package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.web.response.OrganizationPageRetrievalResponse;

public interface OrganizationService {
	Organization getOrganizationById(Long id);
	Organization getOrganizationByAdminIdAndId(Long adminId, Long Id);
	OrganizationPageRetrievalResponse getOrganization(int pageNo, int pageSize);
	OrganizationPageRetrievalResponse getOrganization(long adminId, int pageNo, int pageSize);
	List<Organization> getOrganization();
	List<Organization> getOrganization(long adminId);
	void deleteOrganization(List<Long> ids);
	void deleteOrganization(long adminId,List<Long> ids);
	void alterOrganization(Organization organization);
}
