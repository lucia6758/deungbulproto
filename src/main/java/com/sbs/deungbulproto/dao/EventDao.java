package com.sbs.deungbulproto.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.deungbulproto.dto.Event;

@Mapper
public interface EventDao {

	Event getEvent(Map<String, Object> param);
	
	void addEvent(Map<String, Object> param);

	void updateEvent(Map<String, Object> param);
	
	int getCount(Map<String, Object> param);

	void resetEventCount(Map<String, Object> param);

	void deleteEvent(Map<String, Object> param);

	void deleteEventExceptThisExpert(Map<String, Object> param);

}
