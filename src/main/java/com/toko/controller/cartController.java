package com.toko.controller;

import java.util.Iterator;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.toko.entity.Mailinfo;
import com.toko.entity.Order;
import com.toko.entity.OrderDetail;
import com.toko.mail.MailServiceImpl;
import com.toko.service.OrderService;
import com.toko.service.ProductDetailService;

@Controller
public class cartController {
	@Autowired
	OrderService orderService;

	@Autowired
	ProductDetailService pddService;

	@Autowired
	MailServiceImpl mailService ;
	
	@RequestMapping("/gioHang")
	public String view() {
		return "/cart/gioHang";
	}

	@RequestMapping("/gioHang/checkout")
	public String checkout() {
		return "/cart/checkout";
	}

	@RequestMapping("/purchase/success")
	public String purchaseSuccessful() {
		return "/cart/thanhToanThanhCong";
	}

	@RequestMapping(path = "/cancelURL")
	public String cancelURLPayos(@RequestParam(name = "orderCode", required = false) int orderCode,
			@RequestParam(name = "cancel", required = false) boolean cancel,
			@RequestParam(name = "status", required = false) String status) {

		Order order = orderService.findById(orderCode);
		order.setOrderStatus("CANCEL");
		order.setPaymentStatus(status);
		order.setShippingStatus("DELIVERY FAILED");
		orderService.save(order);
		pddService.updateWhenOrderisCanceled(order.getOrderDetail());
		System.out.println("đã hủy đơn: " + order.getOrderId());

		return "redirect:/";

	}

	@RequestMapping(path = "/returnURL")
	public String returnURLPayos(@RequestParam(name = "orderCode", required = false) int orderCode,
			@RequestParam(name = "cancel", required = false) boolean cancel,
			@RequestParam(name = "status", required = false) String status) {
		if (status.equals("PAID")) {
			Order order = orderService.findById(orderCode);
			order.setOrderStatus("SUCCESS");
			order.setPaymentStatus(status);
			orderService.save(order);
			System.out.println("thanh toán thành công đơn: "+order.getOrderId());
			int amount=0;
			String listItem = "";
			for(OrderDetail item : order.getOrderDetail()) {
				amount+= item.getPrice()*item.getQuantity();
				listItem+= 
				"<div>"
				+"<b>"+item.getName()+" SIZE "+item.getPrice()+"</b>" 
				+"<br>"
				+"&nbsp;&nbsp;"+item.getPrice()+" x "+item.getQuantity()+" <b style=\"margin-left: 40px;\">"+item.getPrice()*item.getQuantity()+"</b>"
				+"<div>";
			}
			int amountAndShip = amount +30000;
			String body = "<p>Xin chào "+order.getCustomerName()+".</p>"
            + "<p>Cảm ơn Anh/Chị đã đặt hàng tại <b>TOKOFASHION</b>.</p>"
            + "<p>Đơn hàng của Anh/Chị đã được tiếp nhận, chúng tôi sẽ nhanh chóng tiến hành giao đơn hàng của Anh/Chị.<p>"
            + "<hr>"
            + "<p><b>Thông tin khách hàng:</b></p>"
            + order.getCustomerName()+ "<br>"
			+ order.getCustomerPhone()+ "<br>"
			+ order.getCustomerEmail()+ "<br>"
			+ order.getCustomerAddress()+ "<br>"
			+ "<p><b>Hình thức thanh toán:</b>"+order.getPaymentMethod()+"</p>"
			+ "<p><b>Ngày đặt hàng:</b>"+order.getOrderDate()+"</p>"
			+ "<hr>"
			+ listItem
			+ "<p>Tổng hóa đơn: <b>"+amount+"</b></p>"
			+ "<p>Phí vận chuyển: <b>"+order.getShippingMoney()+"</b></p>"
			+ "<p>Thành tiền: <b>"+amountAndShip+"</b></p>";
			Mailinfo mailinfo = new Mailinfo(order.getCustomerEmail(),"TOKOFHASION - Đặt hàng thành công",body);
			try {
				mailService.send(mailinfo);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		return "redirect:/purchase/success";
	}

}