package com.h1infotech.smarthive.service;

import java.util.List;
import com.h1infotech.smarthive.domain.Event;

public interface EventService {
	List<Event> getAllEvents();
	
	List<Event> getEventByAdminIdIn(List<Long> adminIds);
	
	List<Event> getNoOrganizationEvents();
}
