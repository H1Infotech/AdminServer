package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import org.springframework.web.bind.annotation.*;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.SmartHiveService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SmartHiveController {
    
	private Logger logger = LoggerFactory.getLogger(SmartHiveController.class);
	
	@Autowired
    private SmartHiveService smartHiveService;

    @GetMapping(path = "/partners")
    @ResponseBody
    public Response<List<Partner>> getPartners() {
        return Response.success(smartHiveService.getPartners());
    }

    @GetMapping(path = "/beeBoxes")
    @ResponseBody
    public Response<List<BeeBox>> getBeeBoxes(HttpServletRequest request) {
    	try {
    		if(request == null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for beeBox: " + request.getHeader("token") + "====");
    		return Response.success(smartHiveService.getBeeBox(request.getHeader("token")));
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }

}
