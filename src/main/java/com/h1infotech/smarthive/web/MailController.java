package com.h1infotech.smarthive.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.service.MailService;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.web.request.EMailRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MailController {

	private static final Logger logger = LoggerFactory.getLogger(MailController.class);

	@Autowired
	private MailService mailService;

	private String to = "472875478@qq.com";

	@RequestMapping("sendMail")
	@ResponseBody
	public Response<String> sendMail(HttpServletRequest httpRequest, @RequestBody EMailRequest eMailRequest) {
		try {
			logger.info("====Catching the Request for Sending Email: {}====", JSONObject.toJSONString(eMailRequest));
			if(eMailRequest==null
					||StringUtils.isEmpty(eMailRequest.getContent().replaceAll("\\s*", ""))
					|| StringUtils.isEmpty(eMailRequest.getTitle().replaceAll("\\s*", ""))) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			mailService.sendSimple(to, eMailRequest.getTitle(), eMailRequest.getContent());
			logger.info("====Service Success====");
			return Response.success(null);
		}catch (BusinessException e) {
        	logger.error(e.getMessage(), e);
            return Response.fail(e.getCode(), e.getMessage());
        } catch(Exception e) {
        	logger.error("Login Error", e);
        	return Response.fail(BizCodeEnum.SERVICE_ERROR);
        }
	}
}
