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
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.web.request.OrganizationRetrievalRequest;
import com.h1infotech.smarthive.web.response.PageOrganizationRetrievalResponse;

@RestController
public class OrganizationController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	OrganizationService organizationService;
	
    @PostMapping(path = "/getPageOrganizations")
    @ResponseBody
	public Response<PageOrganizationRetrievalResponse> getOrganizations(HttpServletRequest httpRequest, @RequestBody OrganizationRetrievalRequest request){
    	try {
    		logger.info("====Catching the Request for Getting Paged Organizations: {}====",JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getBeeFarmerFromToken(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		PageOrganizationRetrievalResponse response = null;
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
    
	@PostMapping(path = "/getAllOrganizations")
	@ResponseBody
	public Response<List<Organization>> getAllOrganizations(HttpServletRequest httpRequest) {
		try {
			logger.info("====Catching the Request for Getting Paged Organizations====");
			Admin admin = jwtTokenUtil.getBeeFarmerFromToken(httpRequest.getHeader("token"));
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
}
