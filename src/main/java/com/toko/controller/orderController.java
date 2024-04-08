package com.toko.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.toko.entity.Accounts;
import com.toko.entity.Order;
import com.toko.entity.OrderDetail;
import com.toko.service.AccountService;
import com.toko.service.OrderDetailService;
import com.toko.service.OrderService;
import com.toko.service.ProductDetailService;

@Controller
public class orderController {

	@Autowired
	OrderService orderService;
	@Autowired
	AccountService accSer;
	@Autowired
	ProductDetailService proDetailService;
	@Autowired
	OrderDetailService orderDetaiService;
	@RequestMapping("/account/MyOrder")
	public String myOrder(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Accounts loadAccount = accSer.findEmail(userDetails.getUsername());

			List<Order> orders1 = orderService.findOrderByStatusSuccess(loadAccount.getAccount_id());
			List<List<String>> arrList1 = new ArrayList<List<String>>();

			for (Order order : orders1) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));

				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList1.add(list);

			}

			List<Order> orders2 = orderService.findOrderByStatusWaiting(loadAccount.getAccount_id());
			List<List<String>> arrList2 = new ArrayList<List<String>>();

			for (Order order : orders2) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList2.add(list);

			}

			List<Order> orders3 = orderService.findOrderByStatusCancel(loadAccount.getAccount_id());
			List<List<String>> arrList3 = new ArrayList<List<String>>();

			for (Order order : orders3) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList3.add(list);

			}

			List<Order> orders = orderService.findOrderByAccId(loadAccount.getAccount_id());
			List<List<String>> arrList = new ArrayList<List<String>>();

			for (Order order : orders) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));

				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList.add(list);

			}
			model.addAttribute("ordersAddTotal", arrList);

			model.addAttribute("success", arrList1);
			model.addAttribute("waiting", arrList2);
			model.addAttribute("cancel", arrList3);

			return "/account/MyOrder";
		} else {
			return "redirect:/account/DangNhap/?returnURL=/account/MyOrder";
		}
	}

	@RequestMapping("/account/MyOrder/Cancel")
	public String cancelOrder( @RequestParam("orderId") Integer orderId, Model model  ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
			
			
			Order orderID = orderService.findByOrderIdUpDate(orderId);
			orderID.setOrderStatus("CANCEL");
			orderID.setShippingStatus("DELIVERY FAILED");
			orderID.setPaymentStatus("CANCELLED");
			List<OrderDetail> orderDetail = orderDetaiService.findByOrderId(orderID.getOrderId());
					proDetailService.updateWhenOrderisCanceled(orderDetail);
			orderService.save(orderID);
			
		
			List<Order> orders1 = orderService.findOrderByStatusSuccess(loadAccount.getAccount_id());
			List<List<String>> arrList1 = new ArrayList<List<String>>();

			for (Order order : orders1) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList1.add(list);

			}

			List<Order> orders2 = orderService.findOrderByStatusWaiting(loadAccount.getAccount_id());
			List<List<String>> arrList2 = new ArrayList<List<String>>();

			for (Order order : orders2) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList2.add(list);

			}

			List<Order> orders3 = orderService.findOrderByStatusCancel(loadAccount.getAccount_id());
			List<List<String>> arrList3 = new ArrayList<List<String>>();

			for (Order order : orders3) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList3.add(list);

			}

			List<Order> orders = orderService.findOrderByAccId(loadAccount.getAccount_id());
			List<List<String>> arrList = new ArrayList<List<String>>();

			for (Order order : orders) {
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(order.getOrderId()));
				list.add(String.valueOf(order.getPaymentStatus()));
				list.add(String.valueOf(order.getShippingStatus()));
				int totalAmount = orderService.findTotalAmount(loadAccount.getAccount_id(), order.getOrderId());
				totalAmount += 30000;
				list.add(String.valueOf(totalAmount));
				list.add(String.valueOf(order.getOrderDate()));
//				list.add(String.valueOf(order.getOrderStatus()));
				if (order.getOrderStatus() == null) {
			        list.add("");
			    } else {
			        list.add(String.valueOf(order.getOrderStatus()));
			    }
				list.add(String.valueOf(order.getPaymentMethod()));
				arrList.add(list);

			}
			model.addAttribute("ordersAddTotal", arrList);

			model.addAttribute("success", arrList1);
			model.addAttribute("waiting", arrList2);
			model.addAttribute("cancel", arrList3);

			return "/account/MyOrder";
		} else {
			return "redirect:/account/DangNhap/?returnURL=/account/MyOrder";
		}
	}
}
