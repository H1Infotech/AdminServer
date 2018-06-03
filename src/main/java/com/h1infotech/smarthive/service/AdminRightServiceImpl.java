package com.h1infotech.smarthive.service;

import java.util.List;
import java.util.LinkedList;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.AdminRight;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.AdminRightRepository;

@Service
public class AdminRightServiceImpl implements AdminRightService {

	@Autowired
	AdminRightRepository adminRightRepository;
	
	@Override
	public List<Integer> getAdminRights(Long adminId) {
		List<AdminRight> adminRights = adminRightRepository.findByAdminIdIs(adminId);
		if(adminRights == null || adminRights.size()==0) {
			return null;
		}
		List<Integer> adminRightIds = new LinkedList<Integer>();
		for(AdminRight right: adminRights) {
			adminRightIds.add(right.getRightId());
		}
		return adminRightIds;
	}

}
