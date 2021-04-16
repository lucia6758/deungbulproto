package com.sbs.deungbulproto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.OrderDao;
import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.Event;
import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.dto.Order;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.util.Util;

@Service
public class OrderService {
	@Autowired
	private EventService eventService;
	@Autowired
	private OrderDao orderDao;

	public Order getOrder(int id) {
		return orderDao.getOrder(id);
	}

	public List<Order> getOrders() {
		return orderDao.getOrders();
	}

	public ResultData addOrder(Map<String, Object> param) {
		orderDao.addOrder(param);
		
		int id = Util.getAsInt(param.get("id"), 0);

		/* 의뢰가 들어오면 이벤트 생성 또는 업데이트 시작 */
		String relTypeCode2 = "expert";
		int relId = id;
		int relId2 = Util.getAsInt(param.get("expertId"), 0);
		String region = (String) param.get("region");
		// 지도사 시나리오 - 의뢰직접요청
		if (relId2 > 0) {
			Map<String, Object> param1 = new HashMap<>();
			param1.put("relTypeCode2", relTypeCode2);
			param1.put("relId", relId);
			param1.put("relId2", relId2);
			param1.put("directOrder", 1);
			// 기존 이벤트 존재여부 체크
			Event event = eventService.getEvent(param1);
			// 기존 이벤트가 있고 directOrder가 0이면 기존 이벤트를 업데이트
			if (event != null && event.getDirectOrder() == 0) {
				eventService.updateEvent(param1);
			}
			// 기존 이벤트가 없으면 새 이벤트 생성
			if (event == null) {
				eventService.addEvent(param1);
			}
		}
		// 지도사 시나리오 - 지역별 요청 
		if (relId2 == 0) {
			Map<String, Object> param2 = new HashMap<>();
			param2.put("relTypeCode2", relTypeCode2);
			param2.put("relId", relId);
			param2.put("relId2", relId2);
			param2.put("region", region);
			/*
			 * // 기존 이벤트 존재여부 체크 Event event = eventService.getEvent(param2); // 기존 이벤트가 있고
			 * region이 ""이면 기존 이벤트를 업데이트 if (event != null && event.getRegion().equals(""))
			 * { eventService.updateEvent(param2); } // 기존 이벤트가 없으면 새 이벤트 생성 if (event ==
			 * null) { eventService.addEvent(param2); }
			 */
			eventService.addEvent(param2);
		}
		
		/* 의뢰가 들어오면 이벤트 생성 또는 업데이트 끝 */

		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData deleteOrder(int id) {
		orderDao.deleteOrder(id);

		return new ResultData("S-1", "요청을 취소하였습니다.", "id", id);
	}

	public ResultData modifyOrder(Map<String, Object> param) {
		orderDao.modifyOrder(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "요청서를 수정하였습니다.", "id", id);
	}

	public Order getForPrintOrder(Integer id) {
		return orderDao.getForPrintOrder(id);
	}

	public List<Order> getForPrintOrders() {
		return orderDao.getForPrintOrders();
	}

	public List<Order> getForPrintOrdersByMemberId(Map<String, Object> param) {
		return orderDao.getForPrintOrdersByMemberId(param);
	}

	public ResultData getClientCanDeleteRd(Order order, Client client) {
		return getActorCanModifyRd(order, client);
	}

	public ResultData getExpertanDeleteRd(Order order, Expert expert) {
		return getActorCanModifyRd(order, expert);
	}

	public ResultData getActorCanModifyRd(Order order, Object actor) {

		// 1. 의뢰인 본인인 경우
		if (actor instanceof Client) {
			Client client = (Client) actor;
			if (order.getClientId() == client.getId()) {
				return new ResultData("S-1", "가능합니다.");
			}
		}
		// 2. 전문가 본인인 경우
		if (actor instanceof Expert) {
			Expert expert = (Expert) actor;
			if (order.getExpertId() == expert.getId()) {
				return new ResultData("S-1", "가능합니다.");
			}
		}

		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData changeStepLevel(int orderId, int nextStepLevel) {
		orderDao.changeStepLevel(orderId, nextStepLevel);

		Order changedOrder = getOrder(orderId);

		/* 진행단계가 변경되면 이벤트 생성 또는 업데이트 시작 */
		String relTypeCode2 = "";
		int relId = orderId;
		int relId2 = 0;
		// 의뢰인 시나리오 3~4
		if (nextStepLevel == 3 || nextStepLevel == 4) {
			relTypeCode2 = "client";
			relId2 = changedOrder.getClientId();
			Map<String, Object> param = new HashMap<>();
			param.put("relTypeCode2", relTypeCode2);
			param.put("relId", relId);
			param.put("relId2", relId2);
			param.put("stepLevel", nextStepLevel);
			// 기존 이벤트 존재여부 체크
			Event event = eventService.getEvent(param);
			// 기존 이벤트가 있고 nextStepLevel이 3,4이면 기존 이벤트를 업데이트
			if (event != null
					&& (event.getStepLevel() == 0 || event.getStepLevel() == 3 || event.getStepLevel() == 4)) {
				eventService.updateEvent(param);
			}
			// 기존 이벤트가 없으면 새 이벤트 생성
			if (event == null) {
				eventService.addEvent(param);
			}
		}
		// 전문가 시나리오 5 + 의뢰인 기존 이벤트있으면 stepLevel 0으로 초기화
		if (nextStepLevel == 5) {
			relTypeCode2 = "expert";
			relId2 = changedOrder.getExpertId();
			Map<String, Object> param = new HashMap<>();
			param.put("relTypeCode2", relTypeCode2);
			param.put("relId", relId);
			param.put("relId2", relId2);
			param.put("stepLevel", nextStepLevel);
			// 기존 이벤트 존재여부 체크
			Event event = eventService.getEvent(param);
			// 기존 이벤트가 있고 nextStepLevel이 0이면 기존 이벤트를 업데이트
			if (event != null && event.getStepLevel() == 0) {
				eventService.updateEvent(param);
			}
			// 기존 이벤트가 없으면 새 이벤트 생성
			if (event == null) {
				eventService.addEvent(param);
			}

			// 의뢰인 기존 이벤트있으면 stepLevel 0으로 초기화
			relTypeCode2 = "client";
			relId2 = changedOrder.getClientId();
			Map<String, Object> param2 = new HashMap<>();
			param2.put("relTypeCode2", relTypeCode2);
			param2.put("relId", relId);
			param2.put("relId2", relId2);
			param2.put("stepLevel", 0);
			// 기존 이벤트 존재여부 체크
			Event event2 = eventService.getEvent(param2);
			// 기존 이벤트가 있고 nextStepLevel이 4이면 기존 이벤트를 업데이트
			if (event2 != null && event2.getStepLevel() == 4) {
				eventService.updateEvent(param2);
			}
		}
		/* 진행단계가 변경되면 이벤트 생성 또는 업데이트 끝 */

		String msg = "요청을 수락하셨습니다.";

		if (nextStepLevel == 3) {
			msg = "장례 진행";
		}
		if (nextStepLevel == 4) {
			msg = "장례종료 확인 요청을 보냈습니다.";
		}
		if (nextStepLevel == 5) {
			msg = "의뢰가 최종 종료되었습니다. 리뷰를 작성해주세요.";
		}

		return new ResultData("S-1", msg, "id", orderId);
	}

	public List<Order> getForPrintExpertOrders(int memberId, String region) {

		return orderDao.getForPrintExpertOrders(memberId, region);
	}

	public void setSetp2(Integer orderId, Integer expertId) {
		orderDao.setSetp2(orderId, expertId);

		Order order = getForPrintOrder(orderId);

		/* 요청이 수락되면 이벤트 생성 또는 업데이트 시작 */
		String relTypeCode2 = "client";
		int relId = orderId;
		int relId2 = order.getClientId();

		Map<String, Object> param = new HashMap<>();
		param.put("relTypeCode2", relTypeCode2);
		param.put("relId", relId);
		param.put("relId2", relId2);
		param.put("accept", 1);
		// 기존 이벤트 존재여부 체크
		Event event = eventService.getEvent(param);
		// 기존 이벤트가 있고 accept가 0이면 기존 이벤트를 업데이트
		if (event != null && event.getAccept() == 0) {
			eventService.updateEvent(param);
		}
		// 기존 이벤트가 없으면 새 이벤트 생성
		if (event == null) {
			eventService.addEvent(param);
		}
		/* 요청이 수락되면 이벤트 생성 또는 업데이트 끝 */
	}

	public void orderReject(Integer orderId, Integer expertId) {
		orderDao.orderReject(orderId, expertId);

		Order order = getForPrintOrder(orderId);

		/* 요청이 거절되면 기존 이벤트 업데이트 시작 */
		String relTypeCode2 = "client";
		int relId = orderId;
		int relId2 = order.getClientId();

		Map<String, Object> param = new HashMap<>();
		param.put("relTypeCode2", relTypeCode2);
		param.put("relId", relId);
		param.put("relId2", relId2);
		param.put("accept", 2);
		// 기존 이벤트 존재여부 체크
		Event event = eventService.getEvent(param);
		// 기존 이벤트가 있고 accept가 1이면 기존 이벤트를 업데이트
		if (event != null && event.getAccept() == 1) {
			eventService.updateEvent(param);
		}
		/* 요청이 거절되면 기존 이벤트 업데이트 끝 */

	}

	public List<Order> getForPrintRequestOrdersByExpertRegion(String region) {
		return orderDao.getForPrintRequestOrdersByExpertRegion(region);
	}

}
