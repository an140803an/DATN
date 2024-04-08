package com.toko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class homeController {

//	@RequestMapping("/")
//	public String index() {
//		return "/home/trangChu";
//	}
//	
//	
	@RequestMapping("/chinhSachDoiTra")
	public String index3() {
		return "/home/chinhSachDoiTra";
	}
//	

//	
	@RequestMapping("/XacThuc")
	public String index8() {
		return "/home/XacThuc";
	}
//	
//	@RequestMapping("/trangChu")
//	public String index9() {
//		return "/home/trangChu";
//	}
//	

	@RequestMapping("/heThongCuaHang")
	public String index13() {
		return "/home/heThongCuaHang";
	}
	
	@RequestMapping("/Profile")
	public String index14() {
		return "/account/Profile";
	}
	

	
	@RequestMapping("/bangsize")
	public String index17() {
		return "/home/detailSize";
	}
	
//	//ADMIN:
//	@RequestMapping("/index")
//	public String index18() {
//		return "/admin/index";
//	}
//	@RequestMapping("/DangKi")
//	public String index19() {
//		return "/admin/DangKi";
//	}
//	@RequestMapping("/dangnhap")
//	public String index20() {
//		return "/admin/DangNhap";
//	}
//	@RequestMapping("/DoiMatKhau")
//	public String index21() {
//		return "/admin/DoiMatKhau";
//	}
//	@RequestMapping("/QLSanPham")
//	public String index22() {
//		return "/admin/QLSanPham";
//	}
//	@RequestMapping("/QLTaiKhoan")
//	public String index25() {
//		return "/admin/QLTaikhoan";
//	}
//	@RequestMapping("/themSP")
//	public String index23() {
//		return "/admin/themSP";
//	}
//	@RequestMapping("/themDM")
//	public String index24() {
//		return "/admin/themDM";
//	}
////	@RequestMapping("/themTK")
////	public String index24() {
////		return "/admin/themTK";
////	}
//	
//	@RequestMapping("/quan-li-san-pham")
//	public String qlsp() {
//		return "/admin/QLSanPham";
//	}
//	
//	
//	@RequestMapping("/quan-li-danh-muc")
//	public String qldm() {
//		return "/admin/QLDanhMuc";
//	}
	
}
