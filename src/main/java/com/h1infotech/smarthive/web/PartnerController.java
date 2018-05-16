package com.h1infotech.smarthive.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.service.PartnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartnerController {
	
	private Logger logger = LoggerFactory.getLogger(PartnerController.class);
	
	@Autowired
	PartnerService partnerService;
	
    @GetMapping(path = "/getPartners")
    @ResponseBody
    public Response<List<Partner>> getPartners(HttpServletRequest request) {
    	logger.info("====Catching the Request for Getting Partner====");
    	return null;
    }
}
