package com.h1infotech.smarthive.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import com.h1infotech.smarthive.domain.Event;
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBoxGroup;
import com.h1infotech.smarthive.common.AdminTypeEnum;
import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeBoxGroupRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    AdminRepository adminRepository;
	
    @Autowired
    EventRepository eventRepository;
	
    @Autowired
    BeeBoxGroupRepository beeBoxGroupRepository;
    
	@Override
	public List<Event> getAllEvents() {
		List<Event> events = eventRepository.findAll();
		if(events==null||events.size()==0) {
			return null;
		}
		List<Long> ids = new LinkedList<Long>();
		for(Event event: events) {
			ids.add(event.getGroupId());
		}
		List<BeeBoxGroup> groups = beeBoxGroupRepository.findByIdIn(ids);
		if(groups==null || groups.size()==0) {
			return events;
		}
		Map<Long, BeeBoxGroup> groupMap = new HashMap<Long, BeeBoxGroup>();
		for(BeeBoxGroup group: groups) {
			groupMap.put(group.getId(), group);
		}
		for(Event event: events) {
			BeeBoxGroup group = groupMap.get(event.getGroupId());
			if(group != null) {
				event.setGroupName(group.getGroupName());
			}
		}
		return events;
	}
	
	@Override
	public List<Event> getEventByAdminIdIn(List<Long> adminIds) {
		List<Event> events = eventRepository.findByAdminIdIn(adminIds);
		if(events==null||events.size()==0) {
			return null;
		}
		List<Long> ids = new LinkedList<Long>();
		for(Event event: events) {
			ids.add(event.getGroupId());
		}
		List<BeeBoxGroup> groups = beeBoxGroupRepository.findByIdIn(ids);
		if(groups==null || groups.size()==0) {
			return events;
		}
		Map<Long, BeeBoxGroup> groupMap = new HashMap<Long, BeeBoxGroup>();
		for(BeeBoxGroup group: groups) {
			groupMap.put(group.getId(), group);
		}
		for(Event event: events) {
			BeeBoxGroup group = groupMap.get(event.getGroupId());
			if(group != null) {
				event.setGroupName(group.getGroupName());
			}
		}
		return events;
	}

	public List<Event> getNoOrganizationEvents() {
		List<Integer> type = new LinkedList<Integer>();
		type.add(AdminTypeEnum.NO_ORGANIZATION_ADMIN.getType());
		List<Admin> admins = adminRepository.findByTypeIn(type);
		if(admins==null || admins.size()==0) {
			return null;
		}
		List<Long> adminIds = new LinkedList<Long>();
		for(Admin one: admins) {
			adminIds.add(one.getId());
		}
		return getEventByAdminIdIn(adminIds);
	}
	
}
