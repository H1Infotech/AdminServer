package com.h1infotech.smarthive.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.h1infotech.smarthive.common.BeeBoxStatusEnum;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.domain.Admin;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.domain.BeeBoxGroupAssociation;
import com.h1infotech.smarthive.domain.BeeFarmer;
import com.h1infotech.smarthive.domain.Event;
import com.h1infotech.smarthive.domain.HistoryAlertEvent;
import com.h1infotech.smarthive.domain.Organization;
import com.h1infotech.smarthive.domain.SensorData;
import com.h1infotech.smarthive.domain.SmsSender;
import com.h1infotech.smarthive.domain.SmsSender.KV;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.repository.BeeBoxGroupAssociationRepository;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;
import com.h1infotech.smarthive.repository.EventRepository;
import com.h1infotech.smarthive.repository.HistoryAlertEventRepository;
import com.h1infotech.smarthive.repository.OrganizationRepository;
import com.h1infotech.smarthive.repository.SensorDataRepository;
import com.h1infotech.smarthive.web.request.RecevierSensorDataRequest;

@Service
public class SensorDataServiceImpl implements SensorDataService {

	private Logger logger = LoggerFactory.getLogger(SensorDataServiceImpl.class);
	
	private final static String NOTIFICATION_SENT_FLAG = "NOTIFICATION_FLAG:";
	
	@Autowired
	SmsSender smsSender;
	@Autowired
	MailService mailService;
	@Autowired
	BeeBoxService beeBoxService;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	BeeBoxRepository beeBoxRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
	BeeFarmerRepository beeFarmerRepository;
	@Autowired
	SensorDataRepository sensorDataRepository;
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	HistoryAlertEventRepository historyAlertEventRepository;
	@Autowired
	SensorDataEvaluationService sensorDataEvaluationService;
	@Autowired
	BeeBoxGroupAssociationRepository beeBoxGroupAssociationRepository;
	
	@Override
	public SensorData getLatestSensorData(Long latestSensorData) {
		Optional<SensorData>  sensorData = sensorDataRepository.findById(latestSensorData);
		if(sensorData.isPresent()) {
			return sensorData.get();
		}
		return null;
	}
	
	public List<SensorData> getSensorDara(List<Long> ids) {
		if(ids==null || ids.size()==0) {
			return null;
		}
		return sensorDataRepository.findByIdIn(ids);
	}
	
	public void insertSensorData(RecevierSensorDataRequest request) {

		logger.info("====Catching the Sensor Data: {}====", JSONObject.toJSONString(request));
		if (request == null || StringUtils.isEmpty(request.getBeeBoxNo())) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		BeeBox beeBox = beeBoxRepository.findByBeeBoxNo(request.getBeeBoxNo());
		if (beeBox == null) {
			throw new BusinessException(BizCodeEnum.NO_BEE_BOX_INFO);
		}

		SensorData sensorData = sensorDataRepository.save(request.getSensorData());

		beeBox.setLatestSensorDataId(sensorData.getId());
		beeBox.setLat(sensorData.getLat());
		beeBox.setLng(sensorData.getLng());
		beeBox.setStatus(BeeBoxStatusEnum.RUNNING_STATUS.getStatus());
		beeBoxRepository.save(beeBox);

		List<Event> events = null;
		Set<Long> ids = new HashSet<Long>();
		List<BeeBoxGroupAssociation> associations = beeBoxGroupAssociationRepository.findByBeeBoxId(beeBox.getId());
		logger.info("====Associations: {}====", JSONArray.toJSONString(associations));
		if (associations != null && associations.size() > 0) {
			for (BeeBoxGroupAssociation one : associations) {
				ids.add(one.getGroupId());
			}
			List<Long> idList = new ArrayList<>(ids);
			events = eventRepository.findByGroupIdIn(idList);
		}
		if (events == null || events.size() == 0) {
			return;
		}
		for (Event event : events) {
			String[] targets = null;
			String[] notificationWays = null;
			if (!StringUtils.isEmpty(event.getNotificationTarget())) {
				targets = event.getNotificationTarget().split(",");
			}
			if (!StringUtils.isEmpty(event.getNotificationWay())) {
				notificationWays = event.getNotificationWay().split(",");
			}
			boolean normalStatus = sensorDataEvaluationService.evaluate(event.getAction(), sensorData);
			if (1 == event.getRuleType() && sensorData.getTemperature() == null) {
				normalStatus = true;
			} else if (2 == event.getRuleType() && sensorData.getHumidity() == null) {
				normalStatus = true;
			} else if (3 == event.getRuleType() && sensorData.getAirPressure() == null) {
				normalStatus = true;
			} else if (4 == event.getRuleType() && sensorData.getBattery() == null) {
				normalStatus = true;
			} else if (5 == event.getRuleType() && sensorData.getGravity() == null) {
				normalStatus = true;
			}
			if (!normalStatus) {
				BeeBox beeBoxDB = beeBoxRepository.findByBeeBoxNo(sensorData.getBeeBoxNo());
				if (beeBoxDB != null) {
					beeBoxDB.setStatus(BeeBoxStatusEnum.ABNORMAL_STATUS.getStatus());
					beeBoxRepository.save(beeBoxDB);
					Optional<BeeFarmer> beeFarmer = beeFarmerRepository.findById(beeBoxDB.getFarmerId());
					if (beeFarmer.isPresent() && !StringUtils.isEmpty(beeFarmer.get().getMobile())) {
						KV[] kvs = new KV[4];
						kvs[0] = new KV("name", beeFarmer.get().getName());
						kvs[1] = new KV("boxNo", "" + beeBoxDB.getBeeBoxNo());
						String dataType = null;
						String value = null;
						if (1 == event.getRuleType()) {
							dataType = "温度";
							value = "" + sensorData.getTemperature();
						} else if (2 == event.getRuleType()) {
							dataType = "湿度";
							value = "" + sensorData.getHumidity();
						} else if (3 == event.getRuleType()) {
							dataType = "压强";
							value = "" + sensorData.getAirPressure();
						} else if (4 == event.getRuleType()) {
							dataType = "电量";
							value = "" + sensorData.getBattery();
						} else if (5 == event.getRuleType()) {
							dataType = "重力";
							value = "" + sensorData.getGravity();
						}
						kvs[2] = new KV("dataType", dataType);
						kvs[3] = new KV("value", value);
						boolean isSent = false;
						if(stringRedisTemplate.opsForValue().get(NOTIFICATION_SENT_FLAG + beeFarmer.get().getMobile())!=null) {
							isSent=true;
						}else {
							stringRedisTemplate.opsForValue().set(NOTIFICATION_SENT_FLAG + beeFarmer.get().getMobile(), "true", 5, TimeUnit.MINUTES);
						}

//						String content = "Hi, " + kvs[0] + "! 您的蜂箱：" + kvs[1] + ", " + kvs[2] + "异常。 当前值：" + kvs[3] + "。";

						if (!isSent && targets != null && "true".equals(targets[0]) && notificationWays != null && "true".equals(notificationWays[1])) {
							if (!StringUtils.isEmpty(beeFarmer.get().getMobile())) {
								logger.info("====Sending Message to Farmer Mobile: {}", beeFarmer.get().getMobile());
								smsSender.dispatchSMSService("2298876", beeFarmer.get().getMobile(), kvs);
							}
						}
						if (!isSent && targets != null && "true".equals(targets[0]) && notificationWays != null && "true".equals(notificationWays[2])) {
							if (!StringUtils.isEmpty(beeFarmer.get().getEmail())) {
								logger.info("====Sending Message to Farmer eMail: {}", beeFarmer.get().getEmail());
//								mailService.sendSimple(beeFarmer.get().getEmail(), "【蜂箱状态异常】", content);
							}
						}
						if (!isSent && targets != null && "true".equals(targets[1]) && notificationWays != null
								&& ("true".equals(notificationWays[1])) || "true".equals(notificationWays[2])) {
							if (beeFarmer.isPresent() && beeFarmer.get().getOrganizationId() != null) {
								Optional<Organization> organization = organizationRepository.findById(beeFarmer.get().getOrganizationId());
								if (organization.isPresent() && organization.get().getAdminId() != null) {
									Optional<Admin> admin = adminRepository.findById(organization.get().getAdminId());
									if ("true".equals(notificationWays[1]) && admin.isPresent()
											&& admin.get().getMobile() != null) {
										logger.info("====Sending Message to Admin Mobile: {}", admin.get().getMobile());
										smsSender.dispatchSMSService("2298876", admin.get().getMobile(), kvs);
									}
									if ("true".equals(notificationWays[2]) && admin.isPresent()
											&& StringUtils.isEmpty(admin.get().getEmail())) {
										logger.info("====Sending Message to Admin eMail: {}", admin.get().getEmail());
//										mailService.sendSimple(admin.get().getEmail(), "【蜂箱状态异常】", content);
									}
								}
							}
						}
						String desc = "蜂箱: " + beeBoxDB.getBeeBoxNo() + ", " + dataType + "异常: " + value;
						HistoryAlertEvent newEvent = new HistoryAlertEvent();
						newEvent.setAdminId(event.getAdminId());
						newEvent.setCreateDate(new Date());
						newEvent.setBeeBoxId(beeBoxDB.getId());
						newEvent.setEvent(desc);
						String handleWay = "方式：";
						if ("true".equals(notificationWays[1])) {
							handleWay += "短信";
							if ("true".equals(notificationWays[2])) {
								handleWay += "，邮件";
							}
						} else if ("true".equals(notificationWays[2])) {
							handleWay += "邮件";
						}
						String notificationTargets = "目标：";
						if ("true".equals(targets[0])) {
							notificationTargets += "蜂农";
							if ("true".equals(targets[1])) {
								notificationTargets += "，管理员";
							}
						} else if ("true".equals(targets[1])) {
							notificationTargets += "，管理员";
						}
						newEvent.setHandleWay(handleWay + ";" + notificationTargets);
						if(!isSent) {
							historyAlertEventRepository.save(newEvent);
						}
					}
				}
			}
		}
	}
}
