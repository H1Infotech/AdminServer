package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.Admin;

public interface AdminService {
	Admin getAdminByUserName(String userName);
}
