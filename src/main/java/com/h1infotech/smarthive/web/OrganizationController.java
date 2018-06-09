package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.AdminRightEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.service.BeeFarmerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.h1infotech.smarthive.web.request.OrganizationAddUpdateRequest;
import com.h1infotech.smarthive.web.request.OrganizationDeletionRequest;
import com.h1infotech.smarthive.web.request.OrganizationPageRetrievalRequest;
import com.h1infotech.smarthive.web.response.OrganizationPageRetrievalResponse;

@RestController
@RequestMapping("/api")
public class OrganizationController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	BeeFarmerService beeFarmerService;
	
    @PostMapping(path = "/getPageOrganizations")
    @ResponseBody
	public Response<OrganizationPageRetrievalResponse> getOrganizations(HttpServletRequest httpRequest, @RequestBody OrganizationPageRetrievalRequest request){
    	try {
    		logger.info("====Catching the Request for Getting Paged Organizations: {}====",JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		OrganizationPageRetrievalResponse response = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			response = organizationService.getOrganization(request.getPageNo(), request.getPageSize());
    			break;
    		case ORGANIZATION_ADMIN:
    			response = organizationService.getOrganization(admin.getId(),request.getPageNo(), request.getPageSize());
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			break;
    		}
    		return Response.success(response);
    	} catch(BusinessException e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Page Organization Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
	}
    
	@GetMapping(path = "/getAllOrganizations")
	@ResponseBody
	public Response<List<Organization>> getAllOrganizations(HttpServletRequest httpRequest) {
		try {
			logger.info("====Catching the Request for Getting Paged Organizations====");
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			List<Organization> organizations = null;
			switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			organizations = organizationService.getOrganization();
    			break;
    		case ORGANIZATION_ADMIN:
    			organizations = organizationService.getOrganization(admin.getId());
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			break;
    		}
			return Response.success(organizations);
		} catch (BusinessException e) {
			logger.error("====Get Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Get Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/deleteOrganizations")
	@ResponseBody
	public Response<String> deleteOrganizations(HttpServletRequest httpRequest, @RequestBody OrganizationDeletionRequest request){
		try {
			logger.info("====Catching the Request for Getting Paged Organizations====");
			if(request==null || request.getIds()==null||request.getIds().size()==0) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (!admin.getRights().contains(AdminRightEnum.ORGANIZATION_MANAGEMENT.getRight())) {
				throw new BusinessException(BizCodeEnum.NO_RIGHT);
			}
			switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			organizationService.deleteOrganization(request.getIds());
    			break;
    		case ORGANIZATION_ADMIN:
    		    organizationService.deleteOrganization(admin.getId(),request.getIds());
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			return Response.fail(BizCodeEnum.NO_RIGHT);
    		}
			beeFarmerService.wipeOutOrganizationId(request.getIds());
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Delete Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Delete Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/alterOrganization")
	@ResponseBody
	public Response<String> alterOrganization(HttpServletRequest httpRequest, @RequestBody OrganizationAddUpdateRequest request){
		try {
			logger.info("====Catching the Request for Getting Paged Organizations====");
			if(request==null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (!admin.getRights().contains(AdminRightEnum.ORGANIZATION_MANAGEMENT.getRight())) {
				throw new BusinessException(BizCodeEnum.NO_RIGHT);
			}
			if(admin.getType()!=AdminTypeEnum.SUPER_ADMIN.getType() && admin.getType()!=AdminTypeEnum.SENIOR_ADMIN.getType()
					&& (request.getAdminId()==null || request.getAdminId().longValue()!=admin.getId().longValue())) {
				throw new BusinessException(BizCodeEnum.NO_RIGHT);
			}
			if(request.getId()!=null) {
				Organization organizationDB = organizationService.getOrganizationByAdminIdAndId(admin.getId(), request.getId());
				if(organizationDB==null) {
					throw new BusinessException(BizCodeEnum.NO_RIGHT);
				}
			}
			Organization organization = request.getId()==null?request.getAddOrganization():request.getUpdateOrganization();
			organizationService.alterOrganization(organization);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Add | Update Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Add | Update Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
}
