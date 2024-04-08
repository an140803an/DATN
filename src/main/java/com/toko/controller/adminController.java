package com.toko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toko.entity.Accounts;
import com.toko.entity.Authorities;
import com.toko.entity.Product;
import com.toko.service.AccountService;
import com.toko.service.AuthorityService;
import com.toko.service.CategoryService;
import com.toko.service.ProductService;

@Controller
//@RequestMapping("/admin")
public class adminController {
	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	AccountService accSer;

	@Autowired
	AuthorityService authSer;
//	@RequestMapping("/QLSanPham")
//	public String index22() {
//		return "/admin/QLSanPham";
//	}

//	@GetMapping("/QLSanPham")
//	public String showProductList(Model model) {
//		try {
//			  System.out.println(" liệu");
//			List<Product> productList = productService.findAll(); // Lấy danh sách sản phẩm từ cơ sở dữ liệu
//			model.addAttribute("products", productList); // Chuyển danh sách sản phẩm đến trang Thymeleaf
//
//			return "admin/QLSanPham"; // Trả về tên trang HTML (product-list.html)
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("lỗi load dữ liệu");
//			return "admin/QLSanPham";
//		}
//		
//	}

	@RequestMapping("/themSP")
	public String index23(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/themSP";
	}

	@RequestMapping("/themDM")
	public String index24(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/themDM";
	}
//	@RequestMapping("/themTK")
//	public String index24() {
//		return "/admin/themTK";
//	}

	@RequestMapping("/quan-li-san-pham")
	public String qlsp(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/QLSanPham";
	}

	@RequestMapping("/quan-li-danh-muc")
	public String qldm(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/QLDanhMuc";
	}

	@RequestMapping("/quan-li-tai-khoan")
	public String qltk(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/QLTaiKhoan";
	}

	@RequestMapping("/bao-cao-doanh-thu")
	public String bcdt(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/index";
	}

	@RequestMapping("/ChaoMung")
	public String welcome(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
		Authorities auth = authSer.findByAccount(loadAccount);
		model.addAttribute("role", auth.getRole().getId());
		return "/admin/welcome";
	}
}
