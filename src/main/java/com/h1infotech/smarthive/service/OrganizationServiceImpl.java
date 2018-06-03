package com.h1infotech.smarthive.service;

import org.slf4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONArray;
import com.h1infotech.smarthive.common.BizCodeEnum;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.web.response.OrganizationPageRetrievalResponse;

@Service(value = "partnerService")
public class OrganizationServiceImpl implements OrganizationService {

	Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Override
	public Organization getOrganizationById(Long id) {
		Optional<Organization> organization = null;
		try {
			organization = organizationRepository.findById(id);
		} catch(Exception e) {
			return null;
		}
		return organization.isPresent()?organization.get():null;
	}
	
	@Override
	public Organization getOrganizationByAdminIdAndId(Long adminId, Long id) {
		return organizationRepository.findByIdAndAdminId(id,adminId);
	}
	
	@Override
	public OrganizationPageRetrievalResponse getOrganization(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<Organization> organizations = organizationRepository.findAll(page);
		OrganizationPageRetrievalResponse response = new OrganizationPageRetrievalResponse();
		if(organizations.hasContent()) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(organizations.getTotalPages());
			response.setOrganizations(organizations.getContent());
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}
	
	@Override
	public OrganizationPageRetrievalResponse getOrganization(long adminId, int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<Organization> organizations = organizationRepository.findByAdminId(adminId, page);
		OrganizationPageRetrievalResponse response = new OrganizationPageRetrievalResponse();
		if(organizations.hasContent()) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(organizations.getTotalPages());
			response.setOrganizations(organizations.getContent());
		}else {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}
		return response;
	}
	
	@Override
	public List<Organization> getOrganization() {
		Iterable<Organization> organizations = organizationRepository.findAll();
		return convertList(organizations);
		
	}
	
	@Override
	public List<Organization> getOrganization(long adminId) {
		return organizationRepository.findByAdminId(adminId);
	}
	
	private List<Organization> convertList(Iterable<Organization> from){
		if(from == null) {
			return null;
		}
		List<Organization> list = new LinkedList<Organization>();
		Iterator<Organization> iterator = from.iterator();
		while(iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	@Override
	@Transactional
	public void deleteOrganization(List<Long> ids) {
		int deleteNum = organizationRepository.deleteByIdIn(ids);
		if(deleteNum != ids.size()) {
			throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
		}
	}
	
	@Override
	@Transactional
	public void deleteOrganization(long adminId, List<Long> ids) {
		int deleteNum = organizationRepository.deleteByAdminIdIsAndIdIn(adminId, ids);
		if(deleteNum != ids.size()) {
			throw new BusinessException(BizCodeEnum.DATABASE_ACCESS_ERROR);
		}
	}
	
	@Override
	@Transactional
	public void alterOrganization(Organization organization) {
		organizationRepository.save(organization);
	}
}
