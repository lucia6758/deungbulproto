package com.sbs.deungbulproto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.EventDao;
import com.sbs.deungbulproto.dto.Event;
import com.sbs.deungbulproto.dto.Order;
import com.sbs.deungbulproto.util.Util;

@Service
public class EventService {

	@Autowired
	private EventDao eventDao;
	@Autowired
	private OrderService orderService;

	public Event getEvent(Map<String, Object> param) {
		return eventDao.getEvent(param);
	}

	public void addEvent(Map<String, Object> param) {
		eventDao.addEvent(param);

	}

	public void updateEvent(Map<String, Object> param) {
		eventDao.updateEvent(param);

	}

	public int getCount(Map<String, Object> param) {
		return eventDao.getCount(param);
	}

	public void resetEventCount(Map<String, Object> param) {
		/* 지도사가 장례 종료 알림 확인 시 해당 이벤트 DB에서 삭제 시작 */
		Map<String, Object> parma1 = new HashMap<>();
		if (param.get("relTypeCode2").equals("expert")) {
			parma1.put("clientId", 0);
			parma1.put("expertId", Util.getAsInt(param.get("relId2"), -1));
			List<Order> orders = orderService.getForPrintOrdersByMemberId(parma1);

			if (orders.size() > 0) {
				for (Order order : orders) {
					param.put("relId", order.getId());
					if (order.getStepLevel() == 5) {
						eventDao.deleteEvent(param);
					}
				}
			}
		}
		/* 지도사가 장례 종료 알림 확인 시 해당 이벤트 DB에서 삭제 끝 */

		eventDao.resetEventCount(param);
	}

	public void deleteEvent(Map<String, Object> param) {
		eventDao.deleteEvent(param);
	}

	public void deleteEventExceptThisExpert(Map<String, Object> param) {
		eventDao.deleteEventExceptThisExpert(param);
	}

}
