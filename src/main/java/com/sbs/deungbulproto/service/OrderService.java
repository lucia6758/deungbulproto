package com.sbs.deungbulproto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.OrderDao;
import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.dto.Order;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.util.Util;

@Service
public class OrderService {
	@Autowired
	private GenFileService genFileService;
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

		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData deleteOrder(int id) {
		orderDao.deleteOrder(id);

		genFileService.deleteGenFiles("order", id);

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

	public ResultData changeStepLevel(int id, int nextStepLevel) {
		orderDao.changeStepLevel(id, nextStepLevel);

		Order changedOrder = getOrder(id);

		Map<String, Object> param = new HashMap<>();
		param.put("religion", changedOrder.getReligion());
		param.put("startDate", changedOrder.getStartDate());
		param.put("endDate", changedOrder.getEndDate());
		param.put("deceasedName", changedOrder.getDeceasedName());
		param.put("bereavedName", changedOrder.getBereavedName());
		param.put("funeralHome", changedOrder.getFuneralHome());
		param.put("region", changedOrder.getRegion());
		param.put("body", changedOrder.getBody());
		param.put("expertId", changedOrder.getExpertId());
		param.put("clientId", changedOrder.getClientId());
		param.put("stepLevel", changedOrder.getStepLevel());

		String msg = "요청을 수락하셨습니다.";

		if (nextStepLevel == 3) {
			msg = "장례종료 확인 요청을 보냈습니다.";
		}
		if (nextStepLevel == 4) {
			msg = "의뢰가 최종 종료되었습니다. 리뷰를 작성해주세요.";
		}

		return new ResultData("S-1", msg, "id", id);
	}

	public List<Order> getForPrintExpertOrders(int memberId, String region) {

		return orderDao.getForPrintExpertOrders(memberId, region);
	}

	public void setSetp2(Integer orderId, Integer expertId) {
		orderDao.setSetp2(orderId, expertId);

	}

	public void orderReject(Integer orderId, Integer expertId) {
		orderDao.orderReject(orderId, expertId);

	}

}
