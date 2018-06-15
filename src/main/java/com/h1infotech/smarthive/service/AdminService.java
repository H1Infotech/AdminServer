package com.h1infotech.smarthive.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.web.request.AdminDeletionRequest;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;

public interface AdminService {
	Admin getAdminByUserName(String userName);
	AdminPageRetrievalResponse getAdmins(long adminId, int pageNo, int pageSize);
	public List<Admin> getAllOrganizationAdmins();
	public List<Admin> getAdmins(long excludeId);
	public Response<String> deleteAdmins(HttpServletRequest httpRequest, AdminDeletionRequest request);
}
