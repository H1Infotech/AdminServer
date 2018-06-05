package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;

public interface AdminService {
	Admin getAdminByUserName(String userName);
	AdminPageRetrievalResponse getAdmins(long adminId, int pageNo, int pageSize);
	public List<Admin> getAllOrganizationAdmins();
}
