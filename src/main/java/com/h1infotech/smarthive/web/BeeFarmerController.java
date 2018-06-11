package com.h1infotech.smarthive.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.common.MyUtils;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.AdminRightEnum;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.web.request.AmbiguousSearchRequest;
import com.h1infotech.smarthive.web.request.BeeFarmerDeletionRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.h1infotech.smarthive.web.request.BeeFarmerAddAndUpdateRequest;
import com.h1infotech.smarthive.web.request.BeeFarmerPageRetrievalRequest;
import com.h1infotech.smarthive.web.response.BeeFarmerPageRetrievalResponse;
import com.h1infotech.smarthive.web.request.OrganizationFarmerDeletionRequest;
import com.h1infotech.smarthive.web.request.PageOrganizationBeeFarmerRequest;
import com.h1infotech.smarthive.web.request.OrganizationBeeFarmerRetrievalRequst;

@RestController
@RequestMapping("/api")
public class BeeFarmerController {
	
	private Logger logger = LoggerFactory.getLogger(BeeFarmerController.class);
	
    @Autowired
    JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	BeeFarmerService beeFarmerService;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @PostMapping(path = "/getPageFarmers")
    @ResponseBody
	public Response<BeeFarmerPageRetrievalResponse> getPageFarmers(HttpServletRequest httpRequest, @RequestBody BeeFarmerPageRetrievalRequest request){
    	try {
    		logger.info("====Catching the Request for Getting Paged Bee Farmers: {}====",JSONObject.toJSONString(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		BeeFarmerPageRetrievalResponse response = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			response = beeFarmerService.getBeeFarmers(request.getPageNo(), request.getPageSize());
    			break;
    		case ORGANIZATION_ADMIN:
    			List<Long> ids = organizationService.getIdsByAdminId(admin.getId());
    			response = beeFarmerService.getBeeFarmers(ids,request.getPageNo(), request.getPageSize());
    			break;
    		case NO_ORGANIZATION_ADMIN:
    			response = beeFarmerService.getBeeFarmersWithoutOrganization(request.getPageNo(), request.getPageSize());
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
    
    @PostMapping(path = "/deleteFarmers")
	@ResponseBody
	public Response<String> deleteFarmers(HttpServletRequest httpRequest, @RequestBody BeeFarmerDeletionRequest request){
		try {
			logger.info("====Catching the Request for Deleting Bee Farmers====");
			if(request==null || request.getIds()==null||request.getIds().size()==0) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (!admin.getRights().contains(AdminRightEnum.BEEFARMER_MANAGEMENT.getRight())) {
				throw new BusinessException(BizCodeEnum.NO_RIGHT);
			}
			beeFarmerService.delete(request.getIds());
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Delete Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Delete Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/alterBeeFarmer")
	@ResponseBody
	public Response<String> alterBeeFarmer(HttpServletRequest httpRequest, @RequestBody BeeFarmerAddAndUpdateRequest request){
		try {
			logger.info("====Catching the Request for Getting Paged Organizations====");
			if(request==null||StringUtils.isEmpty(request.getName())) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (!admin.getRights().contains(AdminRightEnum.BEEFARMER_MANAGEMENT.getRight())) {
				throw new BusinessException(BizCodeEnum.NO_RIGHT);
			}
			BeeFarmer beeFarmer = null;
			if(request.getId()==null) {
				int count = 50;
				String userName = null;
				do {
					userName = MyUtils.getUserName(request.getName());
					beeFarmer = beeFarmerService.getBeeFarmerByUserName(userName);
				}while(beeFarmer==null && count-- > 0);
				if(beeFarmer!=null) {
					throw new BusinessException(BizCodeEnum.REGISTER_USER_EXIST_ERROR);
				}
				beeFarmer = request.getBeeFarmerAdd();
				beeFarmer.setUsername(userName);
				beeFarmer.setPassword(bCryptPasswordEncoder.encode(beeFarmer.getPassword()));
			}else {
				BeeFarmer beeFarmerDB  = beeFarmerService.getBeeFarmerByUserName(request.getUsername());
				if(beeFarmerDB==null) {
					throw new BusinessException(BizCodeEnum.NO_USER_INFO);
				}
				beeFarmer = request.getBeeFarmerUpdate();
				beeFarmer.setCreateDate(beeFarmerDB.getCreateDate());
				beeFarmer.setPassword(beeFarmerDB.getPassword());
			}
			beeFarmerRepository.save(beeFarmer);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Add | Update BeeFarmer Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Add | Update BeeFarmer Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/getOrganizationBeeFarmers")
	@ResponseBody
	public Response<List<BeeFarmer>> getOrganizationBeeFarmers(HttpServletRequest httpRequest, @RequestBody OrganizationBeeFarmerRetrievalRequst request){
		try {
			if(request==null || request.getOrganizationId()==null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			return Response.success(beeFarmerService.getOrganizationBeeFarmers(request.getOrganizationId()));
		} catch (BusinessException e) {
			logger.error("====getOrganizationBeeFarmers Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====getOrganizationBeeFarmers Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/deleteOrganizationFarmer")
	@ResponseBody
	public Response<String> getOrganizationBeeFarmers(HttpServletRequest httpRequest, @RequestBody OrganizationFarmerDeletionRequest request){
		try {
			if(request==null || request.getOrganizationId()==null
					|| request.getFarmerId()==null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			BeeFarmer beeFarmer = beeFarmerRepository.findByIdAndOrganizationId(request.getFarmerId(),request.getOrganizationId());
			beeFarmer.setOrganizationId(null);
			beeFarmerRepository.save(beeFarmer);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====getOrganizationBeeFarmers Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====getOrganizationBeeFarmers Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/searchBeeFarmer")
	@ResponseBody
	public Response<Object> searchBeeFarmer(HttpServletRequest httpRequest,@RequestBody AmbiguousSearchRequest request) {
		try {
			logger.info("====Catching the Request for Search Bee Farmers: {}====", JSONObject.toJSONString(request));
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (request == null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			List<BeeFarmer> beeFarmers = null;
			switch (AdminTypeEnum.getEnum(admin.getType())) {
			case SUPER_ADMIN:
			case SENIOR_ADMIN:
				beeFarmers = beeFarmerService.getAllBeeFarmers();
				break;
			case ORGANIZATION_ADMIN:
				List<Long> ids = organizationService.getIdsByAdminId(admin.getId());
				beeFarmers = beeFarmerService.getBeeFarmers(ids);
				break;
			case NO_ORGANIZATION_ADMIN:
				beeFarmers = beeFarmerService.getBeeFarmersWithoutOrganization();
				break;
			}
			if (beeFarmers != null && beeFarmers.size() > 0) {
				Iterator<BeeFarmer> iterator = beeFarmers.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getDesc().indexOf(request.getKeyword()) == -1) {
						iterator.remove();
					}
				}
			}
			int startIndex = (request.getPageNo() - 1) * request.getPageSize();
			int endIndex = (request.getPageNo()) * request.getPageSize();
			if (endIndex > beeFarmers.size()) {
				endIndex = beeFarmers.size();
			}
			if (startIndex >= beeFarmers.size()) {
				Map<String, Integer> res = new HashMap<String, Integer>();
				res.put("totalPageNo", request.getPageNo());
				res.put("currentPageNo", request.getPageSize());
				return Response.success(res);
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("totalPageNo", request.getPageNo());
			data.put("currentPageNo", request.getPageSize());
			data.put("beeFarmers", beeFarmers);
			return Response.success(data);
		} catch (BusinessException e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/getPageOrganizationBeeFarmers")
	@ResponseBody
	public Response<Object> getPageOrganizationBeeFarmers(HttpServletRequest httpRequest, @RequestBody PageOrganizationBeeFarmerRequest request) {
		try {
			logger.info("====Catching the Request for Search Bee Farmers: {}====", JSONObject.toJSONString(request));
			if (request == null || request.getOrganizationIds() == null || request.getOrganizationIds().size() == 0
					|| request.getPageNo() == null || request.getPageNo() - 1 < 0 || request.getPageSize() == null
					|| request.getPageSize() < 1) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			List<Long> organizationIds = request.getOrganizationIds();
			Integer pageNo = request.getPageNo();
			Integer pageSize = request.getPageSize();
			Pageable page = PageRequest.of(pageNo - 1, pageSize, Sort.Direction.ASC, "id");
			Page<BeeFarmer> beeFarmers = beeFarmerRepository.findByOrganizationIdIn(organizationIds, page);
			Map<String, Object> data = new HashMap<String, Object>();
			if (beeFarmers != null && beeFarmers.getContent() != null && beeFarmers.getContent().size() > 0) {
				data.put("totalPageNo", beeFarmers.getTotalPages());
				data.put("currentPageNo", pageNo);
				data.put("beeFarmers", beeFarmers.getContent());
			} else {
				data.put("totalPageNo", beeFarmers.getTotalPages());
				data.put("currentPageNo", pageNo);
			}
			return Response.success(data);
		} catch (BusinessException e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Get Page Organization Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
}
