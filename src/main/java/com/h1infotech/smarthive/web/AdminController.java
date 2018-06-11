package com.h1infotech.smarthive.web;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.common.MyUtils;
import com.h1infotech.smarthive.common.Response;
import com.h1infotech.smarthive.domain.SmsSender;
import com.h1infotech.smarthive.domain.AdminRight;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.service.AdminService;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.service.BeeFarmerService;
import com.h1infotech.smarthive.common.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.h1infotech.smarthive.repository.AdminRepository;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.h1infotech.smarthive.repository.AdminRightRepository;
import com.h1infotech.smarthive.web.request.AdminDeletionRequest;
import com.h1infotech.smarthive.repository.BeeBoxGroupRepository;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.web.request.AmbiguousSearchRequest;
import com.h1infotech.smarthive.web.request.AdminAlterationRequest;
import com.h1infotech.smarthive.web.request.AdminPageRetrievalRequest;
import com.h1infotech.smarthive.web.response.AdminPageRetrievalResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;

@RestController
@RequestMapping("/api")
public class AdminController {
	
	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	BeeFarmerService beeFarmerService;
	
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	private AdminService adminService;
		
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	
    @Autowired
    private AdminRightRepository adminRightRepository;
	
	@Autowired
	private BeeBoxGroupRepository beeBoxGroupRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;
	
    @GetMapping(path = "/getAdminInfo")
    @ResponseBody
    public Response<Object> getAdminInfo(HttpServletRequest httpRequest){
    	
		try {
			logger.info("====Catching the Request for Getting Paged Admins: {}====");

			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
			if (admin == null) {
				throw new BusinessException(BizCodeEnum.NO_USER_INFO);
			}
			if(admin.getOrganizationId()!=null) {
				Optional<Organization> organization = organizationRepository.findById(admin.getOrganizationId());
				if(organization.isPresent()) {
					admin.setOrganizationName(organization.get().getOrganizationName());
				}
			}
			return Response.success(admin);
		} catch (BusinessException e) {
			logger.error("====Get Paged Admins Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Get Paged Admins Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
    }
	
	//获取分页管理员
    @PostMapping(path = "/getPageAdmins")
    @ResponseBody
	public Response<AdminPageRetrievalResponse> getPageAdmins(HttpServletRequest httpRequest, @RequestBody AdminPageRetrievalRequest request){
    	try {
    		logger.info("====Catching the Request for Getting Paged Admins: {}====",JSONObject.toJSON(request));
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(request==null || request.getPageNo()==null || request.getPageSize()==null) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		AdminPageRetrievalResponse response = null;
    		switch(AdminTypeEnum.getEnum(admin.getType())) {
    		case SUPER_ADMIN:
    		case SENIOR_ADMIN:
    			response = adminService.getAdmins(admin.getId(), request.getPageNo(), request.getPageSize());
    			break;
    		default:
    			return Response.fail(BizCodeEnum.NO_RIGHT);
    		}
    		return Response.success(response);
    	} catch(BusinessException e) {
    		logger.error("====Get Paged Admins Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Paged Admins Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
	}
    
    //获取所有的组织管理员
    @GetMapping(path = "/getAllOrganizationAdmins")
    @ResponseBody
    public Response<List<Admin>> getAllOrganizationAdmins(HttpServletRequest httpRequest) {
    	try {
    		logger.info("====Catching the Request for Getting All Organization Admins====");
    		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
    		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
    		if(admin==null || admin.getType()==null) {
    			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
    		}
    		if(admin.getType()==AdminTypeEnum.ORGANIZATION_ADMIN.getType() 
    				|| admin.getType()==AdminTypeEnum.NO_ORGANIZATION_ADMIN.getType()) {
    			return Response.success(null);
    		}
    		return Response.success(adminService.getAllOrganizationAdmins());
    	} catch(BusinessException e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    }
    
    @PostMapping(path = "/deleteAdmins")
    @ResponseBody
    public Response<String> getAllOrganizationAdmins(HttpServletRequest httpRequest, @RequestBody AdminDeletionRequest request){
    	try {
    		logger.info("====Catching the Request for Deleting Admins：{}====", JSONObject.toJSONString(request));
    		if(request==null || request.getAdminIds()==null || request.getAdminIds().size()==0) {
    			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
    		}
    		adminRepository.deleteByIdIn(request.getAdminIds());
    		List<Organization> organizations = organizationRepository.deleteByAdminIdIn(request.getAdminIds());
    		if(organizations!=null && organizations.size()>0) {
    			List<Long> organizationIds = new LinkedList<Long>();
    			for(Organization organization: organizations) {
    				organizationIds.add(organization.getId());
    			}
    			beeFarmerService.wipeOutOrganizationId(organizationIds);
    		}
    		adminRightRepository.deleteByAdminIdIn(request.getAdminIds());
    		List<BeeBoxGroup> beeBoxGroups = beeBoxGroupRepository.deleteByAdminIdIn(request.getAdminIds());
    		if(beeBoxGroups==null||beeBoxGroups.size()==0) {
    			return Response.success(null);
    		}
    		List<Long> beeBoxGroupIds = new LinkedList<Long>();
    		for(BeeBoxGroup beeBoxGroup: beeBoxGroups) {
    			beeBoxGroupIds.add(beeBoxGroup.getId());
    		}
    		beeBoxGroupAssociationRepository.deleteByGroupIdIn(beeBoxGroupIds);
			return Response.success(null);
    	} catch(BusinessException e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(e.getCode(),e.getMessage());
    	} catch(Exception e) {
    		logger.error("====Get Organization Admin Error====", e);
    		return Response.fail(BizCodeEnum.SERVICE_ERROR);
    	}
    		
    }
    
	@PostMapping(path = "/alterAdmin")
	@ResponseBody
	public Response<Object> alterAdmin(HttpServletRequest httpRequest, @RequestBody AdminAlterationRequest request){
		try {
			logger.info("====Catching the Request for Altering Admin: {}====", JSONObject.toJSONString(request));
			if(request==null
					|| StringUtils.isEmpty(request.getName())) {
				throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
			}
			Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
			logger.info("====Admin: {}====", JSONObject.toJSONString(admin));

			Admin alterAdmin = null;
			
        	String code = stringRedisTemplate.opsForValue().get(SmsSender.VERIFICATION_CODE_KEY_PREFIX+request.getMobile());

        	if(!StringUtils.isEmpty(code)) {
        		if(code.equals(request.getCode())) {
    				throw new BusinessException(BizCodeEnum.WRONG_SMS_CODE);
        		}
        	}
			
			if(request.getId()==null) {
				if(StringUtils.isEmpty(request.getPassword())) {
					throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
				}
				int count = 50;
				String userName = null;
				do {
					userName = MyUtils.getUserName(request.getName());
					alterAdmin = adminRepository.findDistinctFirstByUsername(userName);
				}while(alterAdmin==null && count-- > 0);
				if(alterAdmin!=null) {
					throw new BusinessException(BizCodeEnum.REGISTER_USER_EXIST_ERROR);
				}
				alterAdmin = request.getAdmin();
				alterAdmin.setUsername(userName);
				alterAdmin.setPassword(bCryptPasswordEncoder.encode(alterAdmin.getPassword()));
				Admin savedAdmin = adminRepository.save(alterAdmin);
				if(request.getAdminRight()!=null && request.getAdminRight().size()>0) {
					for(AdminRight adminRight: request.getAdminRight()) {
						adminRight.setAdminId(savedAdmin.getId());
						adminRight.setCreatedBy(admin.getId());
						adminRightRepository.save(adminRight);
					}
				}
			}else {
				Admin adminDB  =  adminRepository.findDistinctFirstByUsername(request.getUsername());
				if(adminDB==null) {
					throw new BusinessException(BizCodeEnum.NO_USER_INFO);
				}
				alterAdmin = request.getAdmin();
				if(StringUtils.isEmpty(alterAdmin.getPassword())) {
					alterAdmin.setPassword(adminDB.getPassword());
				}else {
					alterAdmin.setPassword(bCryptPasswordEncoder.encode(alterAdmin.getPassword()));
				}
				
				alterAdmin.setUpdateDate(adminDB.getUpdateDate());
				adminRightRepository.deleteByAdminId(alterAdmin.getId());
				if(request.getAdminRight()!=null && request.getAdminRight().size()>0) {
					for(AdminRight adminRight: request.getAdminRight()) {
						adminRightRepository.save(adminRight);
					}
				}
			}
			adminRepository.save(alterAdmin);
			return Response.success(null);
		} catch (BusinessException e) {
			logger.error("====Add | Update Admin Error====", e);
			return Response.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("====Add | Update Admin Error====", e);
			return Response.fail(BizCodeEnum.SERVICE_ERROR);
		}
	}
	
	@PostMapping(path = "/searchAdmin")
	@ResponseBody
	public Response<Object> searchAdmin(HttpServletRequest httpRequest, @RequestBody AmbiguousSearchRequest request) {
		logger.info("====Catching the Request for Getting All Organization Admins====");
		Admin admin = jwtTokenUtil.getAdmin(httpRequest.getHeader("token"));
		logger.info("====Admin: {}====", JSONObject.toJSONString(admin));
		if(admin==null) {
			throw new BusinessException(BizCodeEnum.NO_USER_INFO);
		}
		if(StringUtils.isEmpty(request.getKeyword())) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		List<Admin> admins = adminService.getAdmins(admin.getId());
		if(admins!=null && admins.size()>0) {
			Iterator<Admin> iterator = admins.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().getDesc().indexOf(request.getKeyword())==-1) {
					iterator.remove();
				}
			}
		}
		
		int startIndex = (request.getPageNo()-1) * request.getPageSize();
		int endIndex = (request.getPageNo()) * request.getPageSize();
		if(endIndex>admins.size()) {
			endIndex=admins.size();
		}
		if(startIndex>=admins.size()) {
			Map<String,Integer> res = new HashMap<String, Integer>();
			res.put("totalPageNo", request.getPageNo());
			res.put("currentPageNo", request.getPageSize());
			return Response.success(res);
		}
		Map<String,Object> res = new HashMap<String, Object>();
		res.put("totalPageNo", request.getPageNo());
		res.put("currentPageNo", request.getPageSize());
		res.put("admins", admins);
		return Response.success(res);
	}
}
