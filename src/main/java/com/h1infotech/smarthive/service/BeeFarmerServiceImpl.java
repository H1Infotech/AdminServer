package com.h1infotech.smarthive.service;

import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.IntervalSensorDataRepository;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.web.response.BeeFarmerPageRetrievalResponse;

@Service
public class BeeFarmerServiceImpl implements BeeFarmerService{

	Logger logger = LoggerFactory.getLogger(BeeFarmerServiceImpl.class);
	
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	BeeBoxRepository beeBoxRepository;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	@Autowired
	IntervalSensorDataRepository intervalSensorDataRepository;
	
	@Override
	public BeeFarmerPageRetrievalResponse getBeeFarmers(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BeeFarmer> beeFarmers = beeFarmerRepository.findAll(page);
		BeeFarmerPageRetrievalResponse response = new BeeFarmerPageRetrievalResponse();
		if(beeFarmers.hasContent()) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(beeFarmers.getTotalPages());
			response.setBeeFarmers(assembleBeeFarmer(beeFarmers.getContent()));
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}

	@Override
	public BeeFarmer getBeeFarmerByUserName(String username) {
		return beeFarmerRepository.findDistinctFirstByUsername(username);
	}
	
	@Override
	public BeeFarmerPageRetrievalResponse getBeeFarmers(List<Long> ids, int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		if(ids==null) {
			return null;
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BeeFarmer> beeFarmers = beeFarmerRepository.findByOrganizationIdIn(ids, page);
		BeeFarmerPageRetrievalResponse response = new BeeFarmerPageRetrievalResponse();
		if(beeFarmers.hasContent()) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(beeFarmers.getTotalPages());
			response.setBeeFarmers(assembleBeeFarmer(beeFarmers.getContent()));
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}
	
	@Override
	public List<Long> getBeeFarmerIdsWithoutOrganization() {
		List<BeeFarmer> beeFarmers =  beeFarmerRepository.findByOrganizationIdIsNull();
		if(beeFarmers==null || beeFarmers.size()==0) {
			return null;
		}
		List<Long> ids = new LinkedList<Long>();
		for(BeeFarmer beeFarmer: beeFarmers) {
			ids.add(beeFarmer.getId());
		}
		return ids;
	}
	
	@Override
	public BeeFarmerPageRetrievalResponse getBeeFarmersWithoutOrganization(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BeeFarmer> beeFarmers = beeFarmerRepository.findByOrganizationIdIsNull(page);
		BeeFarmerPageRetrievalResponse response = new BeeFarmerPageRetrievalResponse();
		if(beeFarmers.hasContent()) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(beeFarmers.getTotalPages());
			response.setBeeFarmers(assembleBeeFarmer(beeFarmers.getContent()));
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}

	private List<BeeFarmer> assembleBeeFarmer(List<BeeFarmer> beeFarmers) {
		if(beeFarmers==null || beeFarmers.size()==0) {
			return null;
		}
		List<Long> organizationIds = new LinkedList<Long>();
		for(BeeFarmer beeFarmer: beeFarmers) {
			if(beeFarmer.getOrganizationId()!=null) {
				organizationIds.add(beeFarmer.getOrganizationId());
			}
		}
		List<Organization> organizations = organizationRepository.findByIdIn(organizationIds);
		if(organizations==null || organizations.size()==0) {
			return beeFarmers;
		}
		Map<Long, Organization> organizationMap = new HashMap<Long, Organization>();
		for(Organization organization: organizations) {
			organizationMap.put(organization.getId(), organization);
		}
		for(BeeFarmer beeFarmer: beeFarmers) {
			Organization organization = organizationMap.get(beeFarmer.getOrganizationId());
			if(organization!=null) {
				beeFarmer.setOrganizationName(organization.getOrganizationName());
			}
		}
		return beeFarmers;
	}
	
	@Override
	@Transactional
	public void wipeOutOrganizationId(List<Long> organizationIds) {
		if(organizationIds==null || organizationIds.size()==0) {
			return ;
		}
		beeFarmerRepository.wipeOutOrganizationId(organizationIds);
	}
	
	public List<BeeFarmer> getOrganizationBeeFarmers(Long organizationId){
		return beeFarmerRepository.findByOrganizationId(organizationId);
	}
	
	@Override
	public List<Long> getBeeFarmerIdsByOrganizationIdIn(List<Long> organizationIds) {
		if(organizationIds==null || organizationIds.size()==0) {
			return null;
		}
		List<BeeFarmer> beeFarmers = beeFarmerRepository.findByOrganizationIdIn(organizationIds);
		if(beeFarmers == null || beeFarmers.size() == 0) {
			return null;
		}
		List<Long> beeFarmerIds = new LinkedList<Long>();
		for(BeeFarmer beeFarmer: beeFarmers) {
			beeFarmerIds.add(beeFarmer.getId());
		}
		return beeFarmerIds;
	}
	
	@Override
	@Transactional
	public void delete(List<Long> ids) {
		if(ids==null || ids.size()==0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		intervalSensorDataRepository.deleteByFarmerIdIn(ids);
		sensorDataRepository.deleteByFarmerIdIn(ids);
		beeBoxRepository.deleteByFarmerIdIn(ids);
		beeFarmerRepository.deleteByIdIn(ids);
	}

}
