package com.toko.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toko.dao.OrderDAO;
import com.toko.dao.OrdersDetailDAO;
import com.toko.entity.Order;
import com.toko.entity.OrderDetail;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;

@Service
public class OrderService {
	@Autowired
	OrderDAO dao;
	
	@Autowired
	OrdersDetailDAO ddao;
	
	@Autowired
	ProductDetailService pddService;
	
	public List<Order> findAll() {
		List<Order> list = dao.findAll();
		return list;
	}

	public Order findById(Integer id) {
		Order order = dao.findById(id).get();
		return order;
	}
	
	public Order save(Order order) {
		return dao.save(order);
	}
	
	
	public Order saveByJson(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();
		
		Order order = mapper.convertValue(orderData, Order.class );
		
		dao.save(order);
		
		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
		
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetail"), type).stream().peek(d->d.setOrder(order)).collect(Collectors.toList());

		ddao.saveAll(details);
		
//		for(OrderDetail detail : details) {
//			ProductDetail pdd = pddService.findById(detail.getProductDetailId());
//			if(detail.getQuantity() >= pdd.getQuantity()) {
//				pdd.setQuantity(0);
//				pddService.update(pdd);
//			}else {
//				pdd.setQuantity((pdd.getQuantity()-detail.getQuantity()));
//				pddService.update(pdd);
//			}
//		}
		
		
		return order;
		
	}

	public boolean existedById(Integer id) {
		return dao.existsById(id);
	}

	public void deleteById(Integer id) {
		dao.deleteById(id);
	}
	
	public List<Order> findOrderByAccId(Integer id) {
		return dao.findOrderByAccId(id);
	}
	
	
	public int findTotalAmount(Integer accountId, Integer orderID ) {
		return dao.findTotalAmount(accountId, orderID);
	}
	
	
	
//	public List<Order> findOrder(Integer accountID, Integer orderId) {	
//	 return dao.findOrder(accountID, orderId);
//	}
	
	public List<Order> findOrderByStatusSuccess(Integer id) {
		return dao.findOrderByStatusSuccess(id);
	}
	public List<Order> findOrderByStatusWaiting(Integer id) {
		return dao.findOrderByStatusWaiting(id);
	}
	public List<Order> findOrderByStatusCancel(Integer id) {
		return dao.findOrderByStatusCancel(id);
	}
	
	public List<Order> findByOrderId(Integer orderId) {
		return dao.findByOrderId(orderId);
	}

	public Order findByOrderIdUpDate(Integer orderId) {
		return dao.findByOrderIdUpDate(orderId);
	}
	
	
	public int countByDateRange(Date fromDate, Date toDate) {
		return dao.countByDateRange(fromDate, toDate);
	}
	
	public int countOrderStatusByDateRange(String orderStatus,Date fromDate, Date toDate) {
		return dao.countOrderStatusByDateRange(orderStatus, fromDate, toDate);
	}
	
	public int countOSNullByDateRange(Date fromDate, Date toDate) {
		return dao.countOSNullByDateRange(fromDate, toDate);
	}
	
	public int countPaymentMethodByDateRange(String paymentMethod,Date fromDate, Date toDate) {
		return dao.countPaymentMethodByDateRange( paymentMethod, fromDate, toDate);
	}
	
	public int countOSAndPMByDateRange(String paymentMethod,String orderStatus,Date fromDate, Date toDate) {
		return dao.countOSAndPMByDateRange(paymentMethod,orderStatus, fromDate, toDate);
	}
	
	public int countOSNullAndPMByDateRange(String paymentMethod, Date fromDate, Date toDate) {
		return dao.countOSNullAndPMByDateRange(paymentMethod, fromDate, toDate);
	}
	
	public List<Order> findByDateRange(String orderStatus,Date fromDate, Date toDate) {
		return dao.findByDateRange(orderStatus, fromDate, toDate);
	}

}
