package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.web.response.BeeFarmerPageRetrievalResponse;

public interface BeeFarmerService {
//	Organization getById(Long id);
//	Organization getByOrganizationIdAndId(Long organizationId, Long Id);
	void wipeOutOrganizationId(List<Long> organizationIds);
	List<BeeFarmer> getOrganizationBeeFarmers(Long organizatioId);
	List<Long> getBeeFarmerIdsByOrganizationIdIn(List<Long> organizationIds);
	BeeFarmer getBeeFarmerByUserName(String userName);
	BeeFarmerPageRetrievalResponse getBeeFarmers(int pageNo, int pageSize);
	BeeFarmerPageRetrievalResponse getBeeFarmers(List<Long> ids, int pageNo, int pageSize);
	List<Long> getBeeFarmerIdsWithoutOrganization();
	List<BeeFarmer> getBeeFarmersWithoutOrganization();
	List<BeeFarmer> getBeeFarmers(List<Long> ids);
	BeeFarmerPageRetrievalResponse getBeeFarmersWithoutOrganization(int pageNo, int pageSize);

	List<BeeFarmer> getAllBeeFarmers();
//	List<Organization> getOrganization();
//	List<Organization> getOrganization(long adminId);
	void delete(List<Long> ids);
//	void alterOrganization(Organization organization);
}
