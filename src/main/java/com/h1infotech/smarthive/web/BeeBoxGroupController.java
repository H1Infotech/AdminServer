package com.h1infotech.smarthive.web;

import java.util.List;
import java.util.Date;
import org.slf4j.Logger;
import java.util.LinkedList;
import java.util.Collections;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Sort;
import org.apache.commons.lang3.StringUtils;
import com.h1infotech.smarthive.domain.Admin;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.web.request.FilterItem;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;
import com.h1infotech.smarthive.repository.BeeBoxGroupRepository;
import com.h1infotech.smarthive.web.request.SaveBeeBoxGroupRequest;
import com.h1infotech.smarthive.web.request.QueryGroupBeeBoxRequest;
import com.h1infotech.smarthive.web.request.BeeBoxGroupDeletionRequest;
import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;

@RestController
@RequestMapping("/api")
public class BeeBoxGroupController {

	private Logger logger = LoggerFactory.getLogger(BeeBoxGroupController.class);

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	BeeBoxRepository beeBoxRepository;
	
	@Autowired
	BeeFarmerService beeFarmerService;
	
	@Autowired
	OrganizationService organizationService;
		
	@Autowired
	BeeBoxGroupRepository beeBoxGroupRepository;
	
	@Autowired
	BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;

	@GetMapping(path = "/getGroups")
	@ResponseBody
	public Response<List<BeeBoxGroup>> getBeeBoxes(HttpServletRequest request) {
		try {
			logger.info("====Catching the Request for Getting the Groups====");
			Admin admin = jwtTokenUtil.getAdmin(request.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null || admin.getId() == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			Sort sort = new Sort(Sort.Direction.ASC, "id");
			return Response.success(beeBoxGroupRepository.findByAdminId(admin.getId(), sort));
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Login Error", e);
			return Response.fail(BizCodeEnum.LOGIN_ERROR);
		}
	}

	@PostMapping(path = "/deleteGroups")
	@ResponseBody
	public Response<String> deleteGroups(HttpServletRequest httpRequest, @RequestBody BeeBoxGroupDeletionRequest request) {
		try {
			logger.info("====Catching the Request for Deleting Groups: {}====", JSONObject.toJSONString(request));
			if(request==null || request.getIds()==null || request.getIds().size()==0) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			beeBoxGroupAssociationRepository.deleteByGroupIdIn(request.getIds());
			beeBoxGroupRepository.deleteByIdIn(request.getIds());
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Login Error", e);
			return Response.fail(BizCodeEnum.LOGIN_ERROR);
		}
	}
	
	@PostMapping(path = "/queryGroupBeeBox")
	@ResponseBody
	public Response<List<BeeBox>> queryGroupBeeBox(HttpServletRequest httpRequest, @RequestBody QueryGroupBeeBoxRequest request) {
		try {
			logger.info("====Catching the Request for queryGroupBeeBox: {}====", JSONObject.toJSONString(request));
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null || admin.getId() == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if (request == null || request.getFilterItems() == null || request.getFilterItems().size() == 0) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			List<BeeBox> boxes = null;
			List<BeeBox> beeBoxes = new LinkedList<BeeBox>();
			switch (AdminTypeEnum.getEnum(admin.getType())) {
			case SUPER_ADMIN:
			case SENIOR_ADMIN:
				
				for (FilterItem filter : request.getFilterItems()) {
					boxes = beeBoxRepository.findAll(BeeBox.getCondition(filter));
					if (boxes != null && boxes.size() >= 0) {
						beeBoxes.addAll(boxes);
					}
				}
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(beeBoxes);
				}
				return Response.success(beeBoxes);
			case ORGANIZATION_ADMIN:
				List<Long> organizationIds = organizationService.getIdsByAdminId(admin.getId());
				List<Long> beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
				if(beeFarmerIds==null || beeFarmerIds.size()==0) {
					return Response.success(null);
				}
				for (FilterItem filter : request.getFilterItems()) {
					filter.setBeeFarmerIds(beeFarmerIds);
					boxes = beeBoxRepository.findAll(BeeBox.getCondition(filter));
					if (boxes != null && boxes.size() >= 0) {
						beeBoxes.addAll(boxes);
					}
				}
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(beeBoxes);
				}
				return Response.success(beeBoxes);
			case NO_ORGANIZATION_ADMIN:
				beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
				if(beeFarmerIds==null || beeFarmerIds.size()==0) {
					return Response.success(null);
				}
				for (FilterItem filter : request.getFilterItems()) {
					filter.setBeeFarmerIds(beeFarmerIds);
					boxes = beeBoxRepository.findAll(BeeBox.getCondition(filter));
					if (boxes != null && boxes.size() >= 0) {
						beeBoxes.addAll(boxes);
					}
				}
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(beeBoxes);
				}
				return Response.success(beeBoxes);
			}
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Login Error", e);
			return Response.fail(BizCodeEnum.LOGIN_ERROR);
		}
	}
	
	@PostMapping(path="/saveGroupBeeBox")
	@ResponseBody
	public Response<String> saveGroupBeeBox(HttpServletRequest httpRequest, @RequestBody SaveBeeBoxGroupRequest request){
		try {
			logger.info("====Catching the Request for Saving BeeBox Group: {}====", JSONObject.toJSONString(request));
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null || admin.getId() == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if(request==null
					|| request.getBeeBoxGroup()==null 
					|| request.getBeeBoxGroup().getAdminId()==null
					|| StringUtils.isEmpty(request.getBeeBoxGroup().getGroupName())
					|| request.getBeeBoxIds()==null
					|| request.getBeeBoxIds().size()==0) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			BeeBoxGroup beeBoxGroup = beeBoxGroupRepository.findByAdminIdAndGroupName(admin.getId(), request.getBeeBoxGroup().getGroupName());
			if(beeBoxGroup!=null) {
				throw new BusinessException(BizCodeEnum.BEE_BOX_GROUP_EXISTS);
			}
			request.getBeeBoxGroup().setAdminId(admin.getId());
			request.getBeeBoxGroup().setCreateDate(new Date());
			BeeBoxGroup savedBeeBoxGroup = beeBoxGroupRepository.save(beeBoxGroup);
			BeeBoxGroupAssociation beeBoxGroupAssociation = null;
			List<BeeBoxGroupAssociation> associations = new LinkedList<BeeBoxGroupAssociation>();
			for(Long id: request.getBeeBoxIds()) {
				beeBoxGroupAssociation = new BeeBoxGroupAssociation();
				beeBoxGroupAssociation.setBeeBoxId(id);
				beeBoxGroupAssociation.setCreateDate(new Date());
				beeBoxGroupAssociation.setGroupId(savedBeeBoxGroup.getId());
				associations.add(beeBoxGroupAssociation);
			}
			List<BeeBoxGroupAssociation> savedAssociations = beeBoxGroupAssociationRepository.saveAll(associations);
			savedBeeBoxGroup.setBeeBoxNum(savedAssociations.size());
			beeBoxGroupRepository.save(beeBoxGroup);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Login Error", e);
			return Response.fail(BizCodeEnum.LOGIN_ERROR);
		}
	}
}
