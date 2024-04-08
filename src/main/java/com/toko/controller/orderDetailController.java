package com.toko.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toko.dao.OrdersDetailDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Order;
import com.toko.entity.OrderDetail;
import com.toko.entity.Product;
import com.toko.entity.ProductDetail;
import com.toko.service.AccountService;
import com.toko.service.OrderDetailService;
import com.toko.service.OrderService;

@Controller
public class orderDetailController {

	   @Autowired
	    OrderDetailService detailService; 
//	   @Autowired 
//	   OrderDetail orderDetail;
	   @Autowired
	   OrderService orderService;
	
		@Autowired
		AccountService accSer;
	   
	    @RequestMapping("/account/MyOrderDetail/{orderId}")
	    public String myOrderDetail(Model model, @PathVariable("orderId") Integer orderid) {
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	if(authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Accounts loadAccount = accSer.findEmail(userDetails.getUsername());  
	    	
	    	
	    	
	    	
	    	List<Order> orders = orderService.findByOrderId(orderid);
	    	List<String> arrListtotal = new ArrayList<String>();
	    	for (Order order : orders) {
	    		arrListtotal.add(String.valueOf(order.getCustomerName()));
	    		arrListtotal.add(String.valueOf(order.getCustomerEmail()));
	    		arrListtotal.add(String.valueOf(order.getCustomerPhone()));
	    		arrListtotal.add(String.valueOf(order.getCustomerAddress()));
	    		int userDetail = detailService.findOrderDetailsWithTotalAmount(loadAccount.getAccount_id(),orderid);
	    		userDetail +=30000;
    	 		arrListtotal.add(String.valueOf(userDetail));
	    		arrListtotal.add(String.valueOf(order.getOrderDate()));
//	    		System.out.println(arrListtotal);
	    	}
	    		
				List<OrderDetail> tableOrderDetail = detailService.findByOrderDetailId(orderid);
				 
			        model.addAttribute("userOrderDetail", tableOrderDetail); 
		        model.addAttribute("userOrders",arrListtotal );
			        
	    	  
		     return "/account/MyOrderDetail";
	    	}else {
				return "redirect:/account/DangNhap/?returnURL=/account/MyOrder";
			}  
	    	
	    }   
	  

	    
}	    
	    
//	 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	if(authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			Accounts loadAccount = accSer.findEmail(userDetails.getUsername()); 
//			
//			
//			 List<OrderDetail> userDetail = detailService.findOrderWithTotalAmountByOrderId(loadAccount.getAccount_id(),orderId);
//    
//			
//			 List<OrderDetail> tableOrderDetail = detailService.findByOrderId(orderId);
//			 
//		        model.addAttribute("userOrderDetail", tableOrderDetail); 
//		        model.addAttribute("userOrders",userDetail );
//		        
//    	}
       	    
	    
	    
//		@GetMapping("/account/MyOrderDetail/proDetailInOrderDetail/{productId}")
//		public String BackProductDetail(@PathVariable Integer productId, Model model) {
//			
//			Product product = detailService.findProductById(productId);
//			model.addAttribute("BackProDetail", product);
//			return "/product/detail";
//
//		}
    
   

