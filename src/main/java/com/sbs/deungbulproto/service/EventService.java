package com.sbs.deungbulproto.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.EventDao;
import com.sbs.deungbulproto.dto.Event;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	
	public Event getEvent(Map<String, Object> param) {
		return eventDao.getEvent(param);
	}

	public void addEvent(Map<String, Object> param) {
		eventDao.addEvent(param);
		
	}

	public void updateEvent(Map<String, Object> param) {
		eventDao.updateEvent(param);
		
	}

	public int getTotalCount(Map<String, Object> param) {
		return eventDao.getTotalCount(param);
	}

}
