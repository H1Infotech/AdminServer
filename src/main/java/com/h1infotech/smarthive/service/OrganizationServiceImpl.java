package com.h1infotech.smarthive.service;

import org.slf4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.h1infotech.smarthive.common.BizCodeEnum;
import org.springframework.data.domain.PageRequest;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.web.response.PageOrganizationRetrievalResponse;

@Service(value = "partnerService")
public class OrganizationServiceImpl implements OrganizationService {

	Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Override
	public Organization getOrganizationById(Long id) {
		Optional<Organization> partner = null;
		try {
			partner = organizationRepository.findById(id);
		} catch(Exception e) {
			return null;
		}
		return partner.isPresent()?partner.get():null;
	}
	
	@Override
	public PageOrganizationRetrievalResponse getOrganization(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<Organization> organizations = organizationRepository.findAll(page);
		PageOrganizationRetrievalResponse response = new PageOrganizationRetrievalResponse();
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
	public PageOrganizationRetrievalResponse getOrganization(long adminId, int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<Organization> organizations = organizationRepository.findByAdminId(adminId, page);
		PageOrganizationRetrievalResponse response = new PageOrganizationRetrievalResponse();
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

}
