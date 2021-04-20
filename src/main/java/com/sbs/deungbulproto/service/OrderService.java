package com.sbs.deungbulproto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.controller.AdrController;
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
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private AdrController adrController;

	public Order getOrder(int id) {
		return orderDao.getOrder(id);
	}

	public List<Order> getOrders() {
		return orderDao.getOrders();
	}

	public ResultData addOrder(Map<String, Object> param) {
		orderDao.addOrder(param);

		int id = Util.getAsInt(param.get("id"), 0);
		String region = (String) param.get("region");

		/* 의뢰가 들어오면 이벤트 생성 또는 업데이트 시작 */
		String relTypeCode2 = "expert";
		int relId = id;
		int relId2 = Util.getAsInt(param.get("expertId"), 0);

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

			// 푸시 발송
			String[] expertDeviceIdToken = new String[1];
			Expert requestedExpert = expertService.getExpert(relId2);
			expertDeviceIdToken[0] = requestedExpert.getDeviceIdToken();

			try {
				String msg = "의뢰 요청이 들어왔습니다.";
				adrController.send(msg, "", expertDeviceIdToken[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		// 지도사 시나리오 - 지역별 요청
		if (relId2 == 0) {

			List<Expert> experts = expertService.getExpertsForSendSms(region);

			for (Expert expert : experts) {

				Map<String, Object> param2 = new HashMap<>();
				param2.put("relTypeCode2", relTypeCode2);
				param2.put("relId", relId);
				param2.put("relId2", expert.getId());
				param2.put("region", region);

				// 기존 이벤트 존재여부 체크
				Event event = eventService.getEvent(param2);
				// 기존 이벤트가 있고region이 ""이면 기존 이벤트를 업데이트
				if (event != null && event.getRegion().equals("")) {
					eventService.updateEvent(param2);
				}
				// 기존 이벤트가 없으면 새 이벤트 생성
				if (event == null) {
					eventService.addEvent(param2);
				}
			}

			// 지역별 전체 푸시 발송
			String[] expertsDeviceIdToken = null;

			if (experts != null && experts.size() > 0) {
				expertsDeviceIdToken = new String[experts.size()];
				for (int i = 0; i < experts.size(); i++) {
					// Util.sendSms("0100000000", expert.getCellphoneNo(), "장례지도사 의뢰가 올라왔습니다.
					// 링크:~~");
					expertsDeviceIdToken[i] = experts.get(i).getDeviceIdToken();
				}
			}
			try {
				String msg = "지역 의뢰가 올라왔습니다.";
				adrController.send(msg, "", expertsDeviceIdToken);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/* 의뢰가 들어오면 이벤트 생성 또는 업데이트 끝 */

		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData deleteOrder(int id) {
		Order canceledOrder = getOrder(id);

		orderDao.deleteOrder(id);

		/* 요청 취소되면 이벤트 생성 또는 업데이트 시작 */

		// 지도사 시나리오
		String relTypeCode2 = "expert";
		int relId = id;
		int relId2 = Util.getAsInt(canceledOrder.getExpertId(), 0);

		if (relId2 != 0) {

			Map<String, Object> param = new HashMap<>();
			param.put("relTypeCode2", relTypeCode2);
			param.put("relId", relId);
			param.put("relId2", relId2);
			param.put("cancelOrder", 1);
			// 기존 이벤트 존재여부 체크
			Event event = eventService.getEvent(param);
			// 기존 이벤트가 있고 directOrder == 1이지만 확인을 안했으면 이벤트를 DB에서 삭제(알림x)
			if (event != null && event.getDirectOrder() == 1 && event.getAlertCheckStatus() == 0) {
				eventService.deleteEvent(param);
			}
			// 기존 이벤트가 있고 cancelOrder가 0이면 기존 이벤트를 업데이트
			if (event != null && event.getCancelOrder() == 0) {
				eventService.updateEvent(param);
			}
			// 기존 이벤트가 없으면 새 이벤트 생성
			if (event == null) {
				eventService.addEvent(param);
			}
			// 지역 요청을 취소하면 수락했던 전문가를 제외한 해당 지역 이벤트 전체 삭제
			Map<String, Object> param2 = new HashMap<>();
			param2.put("relTypeCode2", relTypeCode2);
			param2.put("relId", relId);
			param2.put("relId2", relId2);
			eventService.deleteEventExceptThisExpert(param2);

			// 남은 다른 의뢰가 없으면 지도사 work 1로 변경
			int expertId = Util.getAsInt(canceledOrder.getExpertId(), 0);

			Map<String, Object> param3 = new HashMap<>();
			param3.put("clientId", 0);
			param3.put("expertId", expertId);

			List<Order> orders = orderDao.getForPrintOrdersByMemberId(param3);

			int inProgressOrderCount = 0;

			for (Order getOrder : orders) {
				if (getOrder.getStepLevel() < 5) {
					inProgressOrderCount++;
				}
			}
			if (inProgressOrderCount == 0) {
				expertService.setWork1(expertId);
			}

			// 푸시 발송
			String[] expertDeviceIdToken = new String[1];
			Expert requestedExpert = expertService.getExpert(relId2);
			expertDeviceIdToken[0] = requestedExpert.getDeviceIdToken();

			try {
				String msg = relId + " 번 의뢰가 취소되었습니다.";
				adrController.send(msg, "", expertDeviceIdToken[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 수락한 전문가가 없었던 경우 해당 지역 이벤트 전체 삭제
		if (relId2 == 0) {
			Map<String, Object> param3 = new HashMap<>();
			param3.put("relTypeCode2", relTypeCode2);
			param3.put("relId", relId);
			eventService.deleteEvent(param3);
		}
		/* 요청이 취소되면 이벤트 생성 또는 업데이트 끝 */

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

			// 푸시 발송
			String[] clientDeviceIdToken = new String[1];
			Client requestedClient = clientService.getClient(relId2);
			clientDeviceIdToken[0] = requestedClient.getDeviceIdToken();

			try {
				String msg = "";
				if (nextStepLevel == 3) {
					msg = relId + " 번 의뢰 장례가 시작되었습니다.";
				}
				if (nextStepLevel == 4) {
					msg = relId + " 번 의뢰 장례가 종료되었습니다.(종료 확인을 해주세요.)";
				}
				adrController.send(msg, "", clientDeviceIdToken[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
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

			// 의뢰인 이벤트 DB에서 삭제
			relTypeCode2 = "client";
			relId2 = changedOrder.getClientId();
			Map<String, Object> param2 = new HashMap<>();
			param2.put("relTypeCode2", relTypeCode2);
			param2.put("relId", relId);
			param2.put("relId2", relId2);
			// 기존 이벤트 존재여부 체크
			Event event2 = eventService.getEvent(param2);
			// 기존 이벤트가 있으면 기존 이벤트를 삭제
			if (event2 != null) {
				eventService.deleteEvent(param2);
			}

			// 푸시 발송
			String[] expertDeviceIdToken = new String[1];
			Expert requestedExpert = expertService.getExpert(relId2);
			expertDeviceIdToken[0] = requestedExpert.getDeviceIdToken();

			try {
				String msg = relId + " 번 의뢰가 '최종 종료' 되었습니다.";
				adrController.send(msg, "", expertDeviceIdToken[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/* 진행단계가 변경되면 이벤트 생성 또는 업데이트 끝 */

		String msg = "요청을 수락하셨습니다.";

		if (nextStepLevel == 3) {
			msg = "장례 진행";
		}
		if (nextStepLevel == 4) {
			msg = "장례종료 '확인 요청'을 보냈습니다.";
		}
		if (nextStepLevel == 5) {
			msg = "의뢰가 '최종 종료'되었습니다. 후기를 작성해주세요.";

			// 남은 의뢰가 없으면 지도사 work 1로 변경
			Map<String, Object> param = new HashMap<>();
			param.put("clientId", 0);
			param.put("expertId", changedOrder.getExpertId());

			List<Order> orders = orderDao.getForPrintOrdersByMemberId(param);

			int inProgressOrderCount = 0;

			for (Order order : orders) {
				if (order.getStepLevel() < 5) {
					inProgressOrderCount++;
				}
			}
			if (inProgressOrderCount == 0) {
				expertService.setWork1(changedOrder.getExpertId());
			}

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
		int relId = orderId;

		// 의뢰인 시나리오
		String relTypeCode2 = "client";
		int relId2 = order.getClientId();

		Map<String, Object> param = new HashMap<>();
		param.put("relTypeCode2", relTypeCode2);
		param.put("relId", relId);
		param.put("relId2", relId2);
		param.put("accept", 1);
		// 기존 이벤트 존재여부 체크
		Event event = eventService.getEvent(param);
		// 기존 이벤트가 있고 accept가 1이 아니면 기존 이벤트를 업데이트
		if (event != null && event.getAccept() != 1) {
			eventService.updateEvent(param);
		}
		// 기존 이벤트가 없으면 새 이벤트 생성
		if (event == null) {
			eventService.addEvent(param);
		}

		// 푸시 발송
		String[] clientDeviceIdToken = new String[1];
		Client requestedClient = clientService.getClient(relId2);
		clientDeviceIdToken[0] = requestedClient.getDeviceIdToken();

		try {
			String msg = "요청하신 " + relId + "번 의뢰가 '수락'되었습니다.";
			adrController.send(msg, "", clientDeviceIdToken[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 전문가 시나리오
		// 지역 요청을 다른 사람이 수락하면 수락한 전문가를 제외한 해당 지역 이벤트 전체 삭제
		relTypeCode2 = "expert";
		relId2 = order.getExpertId();
		Map<String, Object> param2 = new HashMap<>();
		param2.put("relTypeCode2", relTypeCode2);
		param2.put("relId", relId);
		param2.put("relId2", relId2);

		eventService.deleteEventExceptThisExpert(param2);
		// 요청을 수락한 해당 전문가의 이벤트에서 region 컬럼을 초기화
		param2.put("region", "");
		eventService.updateEvent(param2);

		/* 요청이 수락되면 이벤트 생성 또는 업데이트 끝 */
	}

	public void orderReject(Integer orderId, Integer expertId) {
		orderDao.orderReject(orderId, expertId);

		Order order = getForPrintOrder(orderId);
		int relId = orderId;

		/* 요청이 거절되면 이벤트 생성 또는 이벤트 업데이트 시작 */
		// 의뢰인 시나리오
		String relTypeCode2 = "client";
		int relId2 = order.getClientId();

		Map<String, Object> param = new HashMap<>();
		param.put("relTypeCode2", relTypeCode2);
		param.put("relId", relId);
		param.put("relId2", relId2);
		param.put("accept", 2);
		// 기존 이벤트 존재여부 체크
		Event event = eventService.getEvent(param);
		// 기존 이벤트가 있고 accept가 2가 아니면 기존 이벤트를 업데이트
		if (event != null && event.getAccept() != 2) {
			eventService.updateEvent(param);
		}
		// 기존 이벤트가 없으면 새 이벤트 생성
		if (event == null) {
			eventService.addEvent(param);
		}

		// 푸시 발송
		String[] clientDeviceIdToken = new String[1];
		Client requestedClient = clientService.getClient(relId2);
		clientDeviceIdToken[0] = requestedClient.getDeviceIdToken();

		try {
			String msg = "요청하신 " + relId + "번 의뢰가 '거절'되었습니다.";
			adrController.send(msg, "", clientDeviceIdToken[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		// 지도사 시나리오
		// 요청이 거절되면 다른 전문가들의 지역 이벤트 다시 생성
		relTypeCode2 = "expert";
		int needExceptRelId2 = expertId;
		String region = order.getRegion();

		List<Expert> experts = expertService.getExpertsForSendSms(region);

		for (Expert expert : experts) {
			if (needExceptRelId2 != expert.getId()) {
				Map<String, Object> param2 = new HashMap<>();
				param2.put("relTypeCode2", relTypeCode2);
				param2.put("relId", relId);
				param2.put("relId2", expert.getId());
				param2.put("region", region);

				// 기존 이벤트 존재여부 체크
				Event event2 = eventService.getEvent(param2);
				// 기존 이벤트가 있고region이 ""이면 기존 이벤트를 업데이트
				if (event2 != null && event.getRegion().equals("")) {
					eventService.updateEvent(param2);
				}
				// 기존 이벤트가 없으면 새 이벤트 생성
				if (event2 == null) {
					eventService.addEvent(param2);
				}
			}
		}
		/* 요청이 거절되면 이벤트 생성 또는 이벤트 업데이트 끝 */

		// 남은 다른 의뢰가 없으면 지도사 work 1로 변경
		Map<String, Object> param3 = new HashMap<>();
		param3.put("clientId", 0);
		param3.put("expertId", expertId);

		List<Order> orders = orderDao.getForPrintOrdersByMemberId(param3);

		int inProgressOrderCount = 0;

		for (Order getOrder : orders) {
			if (getOrder.getStepLevel() < 5) {
				inProgressOrderCount++;
			}
		}
		if (inProgressOrderCount == 0) {
			expertService.setWork1(expertId);
		}

	}

	public List<Order> getForPrintRequestOrdersByExpertRegion(String region) {
		return orderDao.getForPrintRequestOrdersByExpertRegion(region);
	}

}
