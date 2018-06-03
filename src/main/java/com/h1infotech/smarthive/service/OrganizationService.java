package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.web.response.PageOrganizationRetrievalResponse;

public interface OrganizationService {
	Organization getOrganizationById(Long id);
	PageOrganizationRetrievalResponse getOrganization(int pageNo, int pageSize);
	PageOrganizationRetrievalResponse getOrganization(long adminId, int pageNo, int pageSize);
	List<Organization> getOrganization();
	List<Organization> getOrganization(long adminId);
}
