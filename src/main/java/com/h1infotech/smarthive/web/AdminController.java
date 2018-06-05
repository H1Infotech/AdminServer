package com.h1infotech.smarthive.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.AdminRight;
import com.h1infotech.smarthive.common.MyUtils;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.service.AdminService;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.repository.AdminRightRepository;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.web.request.AdminDeletionRequest;
import com.h1infotech.smarthive.web.request.AdminAlterationRequest;
import com.h1infotech.smarthive.web.request.AdminPageRetrievalRequest;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api")
public class AdminController {
	
	private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    JwtTokenUtil jwtTokenUtil;
	
    @Autowired
    AdminRightRepository adminRightRepository;
    
	@Autowired
	AdminService adminService;
	
	@Autowired
	AdminRepository adminRepository;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @PostMapping(path = "/getPageAdmins")
    @ResponseBody
	public Response<AdminPageRetrievalResponse> getPageAdmins(HttpServletRequest httpRequest, @RequestBody AdminPageRetrievalRequest request){
    	try {
    		logger.info("====Catching the Request for Getting Paged Admins: {}====",JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		AdminPageRetrievalResponse response = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			response = adminService.getAdmins(admin.getId(), request.getPageNo(), request.getPageSize());
    			break;
    		default:
    			return Response.fail(BizCodeEnum.NO_RIGHT);
    		}
    		return Response.success(response);
    	} catch(BusinessException e) {
    		logger.error("====Get Paged Admins Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Paged Admins Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
	}
    
    @GetMapping(path = "/getAllOrganizationAdmins")
    @ResponseBody
    public Response<List<Admin>> getAllOrganizationAdmins(HttpServletRequest httpRequest){
    	try {
    		logger.info("====Catching the Request for Getting All Organization Admins====");
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		return Response.success(adminService.getAllOrganizationAdmins());
    	} catch(BusinessException e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @GetMapping(path = "/deleteAdmins")
    @ResponseBody
    public Response<String> getAllOrganizationAdmins(HttpServletRequest httpRequest, @RequestBody AdminDeletionRequest request){
    	try {
    		logger.info("====Catching the Request for Deleting Admins====");
    		adminRepository.deleteByIdIn(request.getAdminIds());
    		return Response.success(null);
    	} catch(BusinessException e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    		
    }
    
	@PostMapping(path = "/alterAdmin")
	@ResponseBody
	public Response<String> alterAdmin(HttpServletRequest httpRequest, @RequestBody AdminAlterationRequest request){
		try {
			logger.info("====Catching the Request for Altering Admin====");
			if(request==null||StringUtils.isEmpty(request.getName())) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));

			Admin alterAdmin = null;
			if(request.getId()==null) {
				int count = 50;
				String userName = null;
				do {
					userName = MyUtils.getUserName(request.getName());
					alterAdmin = adminRepository.findDistinctFirstByUsername(userName);
				}while(alterAdmin==null && count-- > 0);
				if(alterAdmin!=null) {
					throw new BusinessException(BizCodeEnum.REGISTER_USER_EXIST_ERROR);
				}
				alterAdmin = request.getAdmin();
				alterAdmin.setUsername(userName);
				alterAdmin.setPassword(bCryptPasswordEncoder.encode(alterAdmin.getPassword()));
				if(request.getAdminRight()!=null && request.getAdminRight().size()>0) {
					for(AdminRight adminRight: request.getAdminRight()) {
						adminRightRepository.save(adminRight);
					}
				}
			}else {
				Admin adminDB  =  adminRepository.findDistinctFirstByUsername(request.getUsername());
				if(adminDB==null) {
					throw new BusinessException(BizCodeEnum.NO_USER_INFO);
				}
				alterAdmin = request.getAdmin();
				alterAdmin.setPassword(adminDB.getPassword());
				alterAdmin.setCreateDate(adminDB.getCreateDate());
				adminRightRepository.deleteByAdminId(alterAdmin.getId());
				if(request.getAdminRight()!=null && request.getAdminRight().size()>0) {
					for(AdminRight adminRight: request.getAdminRight()) {
						adminRightRepository.save(adminRight);
					}
				}
			}
			adminRepository.save(alterAdmin);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Add | Update Admin Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Add | Update Admin Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
}
