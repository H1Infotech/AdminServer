package com.h1infotech.smarthive.web;

import java.util.List;
import com.h1infotech.smarthive.domain.Partner;
import com.h1infotech.smarthive.common.Response;
import org.springframework.web.bind.annotation.*;
import com.h1infotech.smarthive.service.BeeBoxService;
import com.h1infotech.smarthive.service.SmartHiveService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SmartHiveController {
	
	@Autowired
	BeeBoxService beeBoxService;
	
	@Autowired
    private SmartHiveService smartHiveService;

    @GetMapping(path = "/partners")
    @ResponseBody
    public Response<List<Partner>> getPartners() {
        return Response.success(smartHiveService.getPartners());
    }
}
