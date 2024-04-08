package com.toko.controller;

import com.toko.entity.Accounts;
import com.toko.entity.Authorities;
import com.toko.entity.Order;
import com.toko.entity.OrderDetail;
import com.toko.service.AccountService;
import com.toko.service.AuthorityService;
import com.toko.service.OrderDetailService;
import com.toko.service.OrderService;
import com.toko.service.ProductDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class QLDonhangController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductDetailService proDetailService;
    @Autowired
    OrderDetailService orderDetailService;
    
    @Autowired
	AccountService accSer;
	
	@Autowired
	AuthorityService authSer;
	@RequestMapping("/QLDonhang")
    public String index(Model model) {
        List<Order> orders = orderService.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role",auth.getRole().getId());
        model.addAttribute("orders", orders);
        return "/admin/QLDonhang";
    }

 
    @GetMapping("/QLDonhang/edit")
    public String adminEdit(Model model, @RequestParam("orderId") Integer orderId) {
        try {
            Order order = orderService.findById(orderId);
            List<Order> orders = orderService.findAll();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
    		Authorities auth = authSer.findByAccount(loadAccount);
    		model.addAttribute("role",auth.getRole().getId());
            model.addAttribute("orders", orders);
            model.addAttribute("order", order);

            return "admin/QLDonhang";
        } catch (NoSuchElementException e) {
           
            return "redirect:/QLDonhang";
        }
    }


    @PostMapping("/QLDonhang/update")
    public String updateOrder(@ModelAttribute("order") @Validated Order order, BindingResult result, Model model) {
    	
    	
        if (!result.hasErrors()) {
            Order orderWithAcc = orderService.findById(order.getOrderId());
            order.setAccount(orderWithAcc.getAccount());
            orderService.save(order);
            if (order.getOrderStatus().equals("CANCEL")) {
            	List<OrderDetail> orderDetail = orderDetailService.findByOrderId(order.getOrderId());
				proDetailService.updateWhenOrderisCanceled(orderDetail);
            }
        }

        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "redirect:/QLDonhang";
    }

}



