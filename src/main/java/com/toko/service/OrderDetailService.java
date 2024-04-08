package com.toko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.toko.dao.OrdersDetailDAO;
import com.toko.dao.ProductDAO;
import com.toko.entity.OrderDetail;

@Service
public class OrderDetailService {

	@Autowired
	OrdersDetailDAO dao;
	@Autowired
	ProductDAO prodao;
	public List<OrderDetail> findAll() {
		List<OrderDetail> list = dao.findAll();
		return list;
	}

	public OrderDetail findById(Integer id) {
		OrderDetail orderDetail = dao.findById(id).get();
		return orderDetail;
	}

	public List<OrderDetail> findByOrderId(Integer id) {
		List<OrderDetail> list = dao.findByOrderId(id);
		return list;
	}

	public OrderDetail save(OrderDetail orderDetail) {
		return dao.save(orderDetail);
	}
	
	public OrderDetail saveByJson(JsonNode orderDetail) {
		return null;
	}

	public boolean existedById(Integer id) {
		return dao.existsById(id);
	}


	public void deleteById(Integer id) {
		dao.deleteById(id);
	}
	
	 public int findOrderDetailsWithTotalAmount(Integer accountId, Integer orderId) {
	        return dao.findOrderDetailsWithTotalAmount(accountId, orderId);
	    }
	
	public List<OrderDetail> findByOrderDetailId(Integer orderId) {
	return	dao.findByOrderDetailId(orderId);
	}
	
	public int totalAmount(Integer orderId) {
		return dao.totalAmountOrders( orderId);
	}
	
	
	
	
	
}
