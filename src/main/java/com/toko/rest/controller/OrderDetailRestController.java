package com.toko.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.toko.entity.OrderDetail;
import com.toko.service.OrderDetailService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orderDetail")
public class OrderDetailRestController {
	@Autowired
	OrderDetailService service;
	
	@GetMapping("{id}")
	public OrderDetail getOne(@PathVariable("id") Integer id) {
		return service.findById(id);
	}
	
//	@PostMapping("/setOneByJson")
//	public void setOne(@RequestBody JsonNode orderDetail) {
//		service.updateByJson(orderDetail);
//		return ;
//				
//	}
}
