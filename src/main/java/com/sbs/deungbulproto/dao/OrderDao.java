package com.sbs.deungbulproto.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.deungbulproto.dto.Order;

@Mapper
public interface OrderDao {
	Order getOrder(@Param("id") int id);

	List<Order> getOrders();

	List<Order> getForPrintOrders();

	void addOrder(Map<String, Object> param);

	void deleteOrder(@Param("id") int id);

	void modifyOrder(Map<String, Object> param);

	Order getForPrintOrder(@Param("id") int id);

	List<Order> getForPrintOrdersByMemberId(Map<String, Object> param);

	void changeStepLevel(@Param("id") int id, @Param("nextStepLevel") int nextStepLevel);

	List<Order> getForPrintExpertOrders(@Param("memberId") int memberId, @Param("region") String region);

	void setSetp2(@Param("orderId") Integer orderId, @Param("expertId") Integer expertId);

	void orderReject(@Param("orderId") Integer orderId, @Param("expertId") Integer expertId);

	List<Order> getForPrintRequestOrdersByExpertRegion(@Param("region") String region);

}
