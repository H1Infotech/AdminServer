package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.BeeBoxService;
import com.h1infotech.smarthive.web.request.LoginRequest;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.web.request.BeeBoxRetrievalRequest;

@RestController
public class BeeBoxController {
	
	private Logger logger = LoggerFactory.getLogger(SmartHiveController.class);
	
	@Autowired
	BeeBoxService beeBoxService;
	
    @GetMapping(path = "/getBeeBoxes")
    @ResponseBody
    public Response<List<BeeBox>> getBeeBoxes(HttpServletRequest request) {
    	try {
    		if(request == null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for beeBox: " + request.getHeader("token") + "====");
    		List<BeeBox> beeBoxes = beeBoxService.getBeeBox(request.getHeader("token"));
    		logger.info("====Response: "+JSONArray.toJSONString(beeBoxes)+"====");
    		return Response.success(beeBoxes);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/getBeeBox")
    @ResponseBody
    public Response<BeeBox> getBeeBoxes(HttpServletRequest request, @RequestBody BeeBoxRetrievalRequest beeBoxRequest) {
    	try {
    		if(request == null || beeBoxRequest==null || beeBoxRequest.getBeeBoxId()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		logger.info("====Catching the Request for beeBox Id: " + beeBoxRequest.getBeeBoxId() +",token: "+request.getHeader("token")+"====");
    		BeeBox beeBox = beeBoxService.getBeeBoxByTokenAndId(request.getHeader("token"), beeBoxRequest.getBeeBoxId());
    		logger.info("====Response: "+JSONArray.toJSONString(beeBox)+"====");
    		return Response.success(beeBox);
    	}catch(BusinessException e) {
    		logger.error(e.getMessage(), e);
    		return Response.fail(e.getCode(), e.getMessage());
    	}catch(Exception e) {
    		logger.error("Get BeeBox Info Error",e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @GetMapping(path = "/addBeeBoxes")
    @ResponseBody
    public Response<List<BeeBox>> getBeeBoxes(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
    	try {
    		if(request == null || loginRequest == null) {
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
    
//    @GetMapping(path = "/deleteBeeBoxes")
//    @ResponseBody
//    public Response<Boolean> deleteBeeBox(HttpServletRequest httpRequest, @RequestBody UserDeleteBeeBoxRequest request) {
//    	try {
//    		
//    	}catch(BusinessException e) {
//    		
//    	}
//    }
}
