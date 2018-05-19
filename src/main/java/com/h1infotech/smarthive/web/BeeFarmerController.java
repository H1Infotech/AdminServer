package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.service.PartnerService;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeeFarmerController {
	
	private Logger logger = LoggerFactory.getLogger(BeeFarmerController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	PartnerService partnerService;
	
    @Autowired
	BeeFarmerService beeFarmerService;
	
	@GetMapping(path = "/getUserDisplayInfo")
    @ResponseBody
    public Response<Object> getBeeFarmerEntity(HttpServletRequest httpRequest) {
    	try {
    		logger.info("====Catching the Request for Getting Bee Farmer( token: " + httpRequest.getHeader("token") + "====");
    		BeeFarmer beeFarmer = jwtTokenUtil.getBeeFarmerFromToken(httpRequest.getHeader("token"));
    		if(beeFarmer.getPartnerId()!=null) {
    			Partner partner = partnerService.getParterById(beeFarmer.getPartnerId());
    			beeFarmer.setPartnerName(partner.getContactName());
    		}
    		return Response.success(beeFarmer);
    	} catch(BusinessException e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("Update Password Error", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
}
