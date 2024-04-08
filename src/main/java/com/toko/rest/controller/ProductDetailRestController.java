package com.toko.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.toko.dao.ProductDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Comment;
import com.toko.entity.OrderDetail;
import com.toko.entity.Product;
import com.toko.entity.ProductDetail;
import com.toko.service.AccountService;
import com.toko.service.CommentService;
import com.toko.service.ProductDetailService;
import com.toko.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/productDetail")
public class ProductDetailRestController {
	@Autowired
	ProductDetailService service;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ProductDAO proDAO;
	
	@Autowired
	ProductService proService;
	
	@GetMapping("{id}")
	public ProductDetail getOne(@PathVariable("id") Integer id) {
		return service.findById(id);
	}
	@GetMapping("/all")
	public List<ProductDetail> getAll() {
		return service.findAll();
	}
	
	@GetMapping("/detail/{id}")
	public List<ProductDetail> getAllProduct(@PathVariable("id") Integer productId) {
		return service.findByProductId(productId);
	}
	
	@GetMapping("/checkStock/{id}/{qty}")
	public boolean checkStock(@PathVariable("id") Integer id, @PathVariable("qty") Integer qty) {
		return service.checkInStock(id, qty);
	}
	
	@PostMapping("/updateByOrderDetailList")
	private void updateByOrderDetailList(@RequestBody List<OrderDetail> orderDetailList ) {
		// TODO Auto-generated method stub
	 service.updateByOrderDetailList(orderDetailList);
	}
	
	
	
	
	
	
	
	
	//Comment
	@GetMapping("/getComment/{productId}")
	public List<Comment> getComment(@PathVariable int productId) {
		return commentService.getComment(productId);
	}

	@PostMapping("/addComment/{productId}")
	public Comment addComment(@PathVariable int productId, @RequestParam String content) {
		System.out.println("productId:" + productId);
		System.out.println("content:" + content);
		Product Products = proDAO.findById(productId).orElse(null);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts account = accountService.findEmail(userDetails.getUsername());
		System.out.println("account.getEmail()" + account.getEmail());
		return commentService.addComment(Products, account, content);
	}
	
//	@PostMapping("/addComment/{productId}")
//	public Comment addComment(@PathVariable int productId, @RequestParam String content) {
//		System.out.println("productId:" + productId);
//		System.out.println("content:" + content);
//		Product Products = proDAO.findById(productId).orElse(null);
//
//		// Authentication authentication =
//		// SecurityContextHolder.getContext().getAuthentication();
//		// UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		// System.out.println("userDetails.getUsername()" + thuanhmps22390@fpt.edu.vn);
//		Accounts account = accountService.findEmail("thuanhmps22390@fpt.edu.vn");
//		System.out.println("account.getEmail()" + account.getEmail());
//		return commentService.addComment(Products, account, content);
//	}
	
	
	
	//update one
	@PutMapping("/quantity/{id}/{qty}")
	public ProductDetail updateQuantity(@RequestBody ProductDetail detail, @PathVariable("id") Integer id, @PathVariable("qty") Integer qty) {
	    ProductDetail productDetail = service.findById(id);
	    if (productDetail != null) {
	        productDetail.setQuantity(qty);
	        service.update(productDetail);
	        return productDetail;
	    } else {
	        throw new RuntimeException("Không tìm thấy ProductDetail có id: " + id);
	    }
	}
	
	
	
	//create one
//	@PostMapping("/size/{id}")
//	public ProductDetail createSize(@RequestBody ProductDetail detail, @PathVariable("id") Integer id, @PathVariable("size") String size) {
//		ProductDetail productDetail = service.findById(id);
//		if(productDetail != null) {
//			productDetail.setSize(size);
//			service.create(detail);
//			return detail;
//		}else {
//	        throw new RuntimeException("Không tìm thấy ProductDetail có id: " + id);
//	    }
//		
//	}
	
//	@PostMapping("/size/{id}/{size}")
//	public ProductDetail createSize(@RequestBody ProductDetail detail, @PathVariable("id") Integer id, @PathVariable("size") String size) {
//	    ProductDetail productDetail = service.findById(id);
//	    if (productDetail != null) {
//	        productDetail.setSize(size);
//	        service.update(productDetail);
//	        return productDetail;
//	    } else {
//	        throw new RuntimeException("Không tìm thấy ProductDetail có id: " + id);
//	    }
//	}
	
//	@PostMapping("/size/{id}")
//	public ProductDetail createSize(@PathVariable("id") Integer id, @RequestBody ProductDetail productDetail) {
//	    try {
//	        ProductDetail existingProductDetail = service.findById(id);
//	        if (existingProductDetail != null) {
//	            existingProductDetail.setSize(productDetail.getSize());
//	            service.update(existingProductDetail);
//	            return existingProductDetail;
//	        } else {
//	            throw new RuntimeException("Không tìm thấy ProductDetail có id: " + id);
//	        }
//	    } catch (Exception e) {
//	        throw new RuntimeException("Failed to create new size for ProductDetail", e);
//	    }
//	}
	
	@PostMapping("/size/{productId}/{size}/{qty}")
	public ProductDetail createSize(@PathVariable("qty") Integer qty, @PathVariable("productId") Integer productId, @PathVariable("size") String size) {
		Product product = proService.findById(productId);
		ProductDetail detail = new ProductDetail();
		detail.setProduct(product);
		detail.setQuantity(qty);
		detail.setSize(size);
		
		return service.create(detail);
	}

}
