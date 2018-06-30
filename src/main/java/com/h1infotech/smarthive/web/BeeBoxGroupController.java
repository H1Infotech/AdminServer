package com.h1infotech.smarthive.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import org.slf4j.Logger;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.h1infotech.smarthive.domain.Admin;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.web.request.FilterItem;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.service.OrganizationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.SensorData;
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
	private BeeFarmerRepository beeFarmerRepository;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	BeeBoxRepository beeBoxRepository;
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
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
	
	@PostMapping(path = "/getGroupBeeBox")
	@ResponseBody
	public Response<List<BeeBox>> queryGroupBeeBox(HttpServletRequest httpRequest, @RequestBody Map<String, Object> request) {
		try {
			logger.info("====Catching the Request for Deleting Groups: {}====", JSONObject.toJSONString(request));
			if(request==null || request.get("groupId")==null) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			List<BeeBoxGroupAssociation> associations = beeBoxGroupAssociationRepository.findByGroupId((Long)request.get("groupId"));
			if(associations==null) {
				return Response.success(null);
			}
			Set<Long> beeBoxIds = new HashSet<Long>();
			for(BeeBoxGroupAssociation association: associations) {
				beeBoxIds.add(association.getBeeBoxId());
			}
			List<Long> beeBoxIdList = new ArrayList<>(beeBoxIds);
			List<BeeBox> beeBoxes = beeBoxRepository.findByIdIn(beeBoxIdList);
			return Response.success(beeBoxes);
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
	public Response<Object> queryGroupBeeBox(HttpServletRequest httpRequest, @RequestBody QueryGroupBeeBoxRequest request) {
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
			for(FilterItem filter: request.getFilterItems()) {
				if(filter.getMinBeeBoxNo()!=null && filter.getMaxBatchNo()!=null 
						&& filter.getMinBeeBoxNo().equals(filter.getMaxBatchNo())) {
					filter.setBatchNo(filter.getMinBeeBoxNo());
					filter.setMinBatchNo(null);
					filter.setMaxBatchNo(null);
				}
				if(filter.getMinBeeBoxNo()!=null && filter.getMaxBeeBoxNo()!=null
						&& filter.getMinBeeBoxNo().equals(filter.getMaxBeeBoxNo())) {
					filter.setBeeBoxno(filter.getMinBeeBoxNo());
					filter.setMinBeeBoxNo(null);
					filter.setMaxBeeBoxNo(null);
				}
				if(filter.getMinBatchNo()!=null && filter.getMaxBatchNo()!=null
						&& filter.getMinBatchNo().equals(filter.getMaxBatchNo())) {
					filter.setBatchNo(filter.getMinBatchNo());
					filter.setMinBatchNo(null);
					filter.setMaxBatchNo(null);
				}
			}
			List<BeeBox> boxes = null;
			List<Long> organizationIds = null;
			List<Long> beeFarmerIds = null;
			Set<Long> beeBoxIds = new HashSet<Long>();
			List<Long> lastestSensorData = new LinkedList<Long>();
			List<BeeBox> beeBoxes = new LinkedList<BeeBox>();
			List<SensorData> sensorData = new LinkedList<SensorData>();
			switch (AdminTypeEnum.getEnum(admin.getType())) {
			case SUPER_ADMIN:
			case SENIOR_ADMIN:
				for (FilterItem filter : request.getFilterItems()) {
					Specification<BeeBox> specs = getCondition(filter);
					boxes = beeBoxRepository.findAll(specs);
					if (boxes != null && boxes.size() >= 0) {
						for(BeeBox beeBox: boxes) {
							if(!beeBoxIds.contains(beeBox.getId())) {
								if(beeBox.getLatestSensorDataId()==null) {
									SensorData data = new SensorData();
									data.setBeeBoxNo(beeBox.getBeeBoxNo());
									data.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
									sensorData.add(data);
								}else {
									lastestSensorData.add(beeBox.getLatestSensorDataId());
								}
								beeBoxes.add(beeBox);
								beeBoxIds.add(beeBox.getId());
							}
						}
					}
					if(lastestSensorData.size()>0) {
						List<SensorData> sensorDataDB = sensorDataRepository.findByIdIn(lastestSensorData);
						if(sensorDataDB.size()>0) {
							sensorData.addAll(sensorDataDB);
						}
						lastestSensorData.clear();
					}
				}
				
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(sensorData);
				}
				return Response.success(sensorData);
			case ORGANIZATION_ADMIN:
				organizationIds = organizationService.getIdsByAdminId(admin.getId());
				if(organizationIds!=null && organizationIds.size()>0) {
					beeFarmerIds = beeFarmerService.getBeeFarmerIdsByOrganizationIdIn(organizationIds);
					if(beeFarmerIds==null || beeFarmerIds.size()==0) {
						return Response.success(null);
					}
					for (FilterItem filter : request.getFilterItems()) {
						filter.setBeeFarmerIds(beeFarmerIds);
						boxes = beeBoxRepository.findAll(getCondition(filter));
						if (boxes != null && boxes.size() >= 0) {
							for(BeeBox beeBox: boxes) {
								if(!beeBoxIds.contains(beeBox.getId())) {
									if(beeBox.getLatestSensorDataId()==null) {
										SensorData data = new SensorData();
										data.setBeeBoxNo(beeBox.getBeeBoxNo());
										data.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
										sensorData.add(data);
									}else {
										lastestSensorData.add(beeBox.getLatestSensorDataId());
									}
									beeBoxes.add(beeBox);
									beeBoxIds.add(beeBox.getId());
								}
							}
						}
					}
					if(lastestSensorData.size()>0) {
						List<SensorData> sensorDataDB = sensorDataRepository.findByIdIn(lastestSensorData);
						if(sensorDataDB.size()>0) {
							sensorData.addAll(sensorDataDB);
						}
						lastestSensorData.clear();
					}
				}
				
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(sensorData);
				}
				return Response.success(sensorData);
			case NO_ORGANIZATION_ADMIN:
				beeFarmerIds = beeFarmerService.getBeeFarmerIdsWithoutOrganization();
				if(beeFarmerIds==null || beeFarmerIds.size()==0) {
					return Response.success(null);
				}
				for (FilterItem filter : request.getFilterItems()) {
					filter.setBeeFarmerIds(beeFarmerIds);
					Specification<BeeBox> specs = getCondition(filter);
					boxes = beeBoxRepository.findAll(specs);
					if (boxes != null && boxes.size() >= 0) {
						for(BeeBox beeBox: boxes) {
							if(!beeBoxIds.contains(beeBox.getId())) {
								if(beeBox.getLatestSensorDataId()==null) {
									SensorData data = new SensorData();
									data.setBeeBoxNo(beeBox.getBeeBoxNo());
									data.setStatus(BeeBoxStatusEnum.OFFLINE_STATUS.getStatus());
									sensorData.add(data);
								}else {
									lastestSensorData.add(beeBox.getLatestSensorDataId());
								}
								beeBoxes.add(beeBox);
								beeBoxIds.add(beeBox.getId());
							}
						}
					}
					if(lastestSensorData.size()>0) {
						List<SensorData> sensorDataDB = sensorDataRepository.findByIdIn(lastestSensorData);
						if(sensorDataDB.size()>0) {
							sensorData.addAll(sensorDataDB);
						}
						lastestSensorData.clear();
					}
				}
				if(beeBoxes!=null && beeBoxes.size()>0) {
					Collections.sort(sensorData);
				}
				return Response.success(sensorData);
			}
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("queryGroupBeeBox", e);
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
			BeeBoxGroup savedBeeBoxGroup = beeBoxGroupRepository.save(request.getBeeBoxGroup());
			BeeBoxGroupAssociation beeBoxGroupAssociation = null;
			List<BeeBoxGroupAssociation> associations = new LinkedList<BeeBoxGroupAssociation>();
			for(Long id: request.getBeeBoxIds()) {
				beeBoxGroupAssociation = new BeeBoxGroupAssociation();
				beeBoxGroupAssociation.setBeeBoxId(id);
				beeBoxGroupAssociation.setCreateDate(new Date());
				beeBoxGroupAssociation.setGroupId(savedBeeBoxGroup.getId());
				associations.add(beeBoxGroupAssociation);
				Optional<BeeBox> beeBox = beeBoxRepository.findById(id);
				if(beeBox!=null && beeBox.isPresent()) {
					beeBox.get().setProtectionStrategy(true);
					beeBoxRepository.save(beeBox.get());
				}
			}
			List<BeeBoxGroupAssociation> savedAssociations = beeBoxGroupAssociationRepository.saveAll(associations);
			savedBeeBoxGroup.setBeeBoxNum(savedAssociations.size());
			beeBoxGroupRepository.save(savedBeeBoxGroup);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("Login Error", e);
			return Response.fail(BizCodeEnum.LOGIN_ERROR);
		}
	}
	
	public Specification<BeeBox> getCondition(FilterItem filterItem) {
		return new Specification<BeeBox>() {
			private static final long serialVersionUID = -4421852559886689923L;
			@Override
			public Predicate toPredicate(Root<BeeBox> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				System.err.println("====Filter: " + JSONObject.toJSONString(filterItem) + "====");

				List<Predicate> predicates = new ArrayList<>();
				if(filterItem.getBeeFarmerIds()!=null && filterItem.getBeeFarmerIds().size()>0) {
					Expression<Long> exp = root.<Long>get("farmerId");
					predicates.add(exp.in(filterItem.getBeeFarmerIds()));
				}
				if (!StringUtils.isEmpty(filterItem.getBeeBoxno())) {
					predicates.add(cb.equal(root.<String>get("beeBoxNo"), filterItem.getBeeBoxno()));
				}
				if (!StringUtils.isEmpty(filterItem.getMaxBeeBoxNo())) {
					predicates.add(cb.lessThanOrEqualTo(root.<String>get("beeBoxNo"), filterItem.getMaxBeeBoxNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getMinBeeBoxNo())) {
					predicates.add(cb.greaterThanOrEqualTo(root.<String>get("beeBoxNo"), filterItem.getMinBeeBoxNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getBeeFarmerName())) {
					List<BeeFarmer> beeFarmers = beeFarmerRepository.findByNameLike("%" + filterItem.getBeeFarmerName() + "%");
					List<Long> ids = new LinkedList<Long>();
					for(BeeFarmer beeFarmer: beeFarmers) {
						ids.add(beeFarmer.getId());
					}
					if (ids != null && ids.size() > 0) {
						predicates.add(root.<Long>get("farmerId").in(ids));
					}
				}
				if (filterItem.getBeeFarmerId() != null) {
					Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(filterItem.getBeeFarmerId());
					if (beeFarmer != null) {
						predicates.add(cb.equal(root.<Long>get("farmerId"), beeFarmer.get().getId()));
					}
				}
				if (!StringUtils.isEmpty(filterItem.getBatchNo())) {
					predicates.add(cb.like(root.<String>get("batchNo"), "%" + filterItem.getBatchNo() + "%"));
				}
				if (!StringUtils.isEmpty(filterItem.getMinBatchNo())) {
					predicates.add(cb.greaterThanOrEqualTo(root.<String>get("batchNo"), filterItem.getMinBatchNo()));
				}
				if (!StringUtils.isEmpty(filterItem.getMaxBatchNo())) {
					predicates.add(cb.lessThanOrEqualTo(root.<String>get("batchNo"), filterItem.getMaxBatchNo()));
				}
				if (filterItem.getProductionDate() != null) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(filterItem.getProductionDate());
					calendar.add(Calendar.DATE, 1);
					Date maxDate = calendar.getTime();
					predicates.add(cb.between(root.<Date>get("productionDate"), filterItem.getProductionDate(), maxDate));
				}
				if(!StringUtils.isEmpty(filterItem.getManfaucturer())) {
					predicates.add(cb.like(root.<String>get("manufacturer"), "%" + filterItem.getManfaucturer() + "%"));
				}
				if (filterItem.getMinLatitude() != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("lat"), filterItem.getMinLatitude()));
				}
				if (filterItem.getMaxLatitude() != null) {
					predicates.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("lat"), filterItem.getMaxLatitude()));
				}
				if (filterItem.getMinLongitude() != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("lng"), filterItem.getMinLongitude()));
				}
				if (filterItem.getMaxLongitude() != null) {
					predicates.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("lng"), filterItem.getMaxLongitude()));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
