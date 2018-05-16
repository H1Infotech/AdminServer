package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.BeeBoxService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class BeeBoxController {
	private Logger logger = LoggerFactory.getLogger(SmartHiveController.class);
	
	@Autowired
	BeeBoxService beeBoxService;
	
    @GetMapping(path = "/beeBoxes")
    @ResponseBody
    public Response<List<BeeBox>> getBeeBoxes(HttpServletRequest request) {
    	try {
    		if(request == null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for beeBox: " + request.getHeader("token") + "====");
    		return Response.success(beeBoxService.getBeeBox(request.getHeader("token")));
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
}
