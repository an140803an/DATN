package com.toko.controller;

import java.io.File;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.toko.dao.AccountDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Authorities;
import com.toko.entity.ForgotPassword;
import com.toko.entity.Mailinfo;
import com.toko.entity.Role;
import com.toko.mail.MailServiceImpl;
import com.toko.security.UserDetailsImpl;
import com.toko.security.UserDetailsServiceImpl;

import com.toko.service.AccountService;
import com.toko.service.AuthorityService;
import com.toko.service.ForgotPasswordService;
import com.toko.service.VerifyUtil;
import com.toko.service.RoleService;
import com.toko.service.SessionService;

@Controller
public class accountController {

	@Autowired
	AccountService accSer;

	@Autowired
	AuthorityService authSer;

	@Autowired
	RoleService roleSer;

	@Autowired
	UserDetailsServiceImpl usSer;

	@Autowired
	VerifyUtil verifyUtil;

	@Autowired
	MailServiceImpl mailimpl;

	@Autowired
	ForgotPasswordService forgotSer;

	@Autowired
	SessionService session;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping("/account/Profile")
	public String profile(Model model, @ModelAttribute("us") Accounts acc) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Accounts loadAccount = accSer.findEmail(userDetails.getUsername());
				Accounts newAcc = new Accounts();
				newAcc.setEmail(loadAccount.getEmail());
				newAcc.setName(loadAccount.getName());
				newAcc.setPhone(loadAccount.getPhone());
				newAcc.setAddress(loadAccount.getAddress());
				newAcc.setImg(loadAccount.getImg());
				newAcc.setGender(loadAccount.getGender());
				Authorities auth = authSer.findByAccount(loadAccount);
				model.addAttribute("role",auth.getRole().getId());
				model.addAttribute("us", newAcc);
				return "/account/Profile";
			} else {
				return "redirect:/account/DangNhap/?returnURL=/account/Profile";
			}
		} catch (Exception e) {
			System.out.println(e);
			return "/account/Profile";
		}
	}

	@PostMapping("/Profile/changeProfile")
	public String changeProfile(Model model, @ModelAttribute("us") Accounts acc, @RequestParam(name = "exampleRadios", required = false) String exampleRadios,
			@RequestParam String DiaChi, @RequestParam(name = "saveIMG", required = false) MultipartFile file) {
		if (file.getOriginalFilename() != "") {
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Accounts updateAcc = accSer.findEmail(userDetails.getUsername());
				updateAcc.setName(acc.getName());
				updateAcc.setAddress(DiaChi);
				updateAcc.setPhone(acc.getPhone());
				updateAcc.setGender(exampleRadios);
				updateAcc.setImg(file.getOriginalFilename());
				// Xác định đường dẫn tương đối đến thư mục trong dự án
				String relativePath = "static/assets/image/";
				// Lấy đối tượng Resource từ đường dẫn tương đối
				Resource resource = resourceLoader.getResource("classpath:" + relativePath);
				// Lấy File tương ứng với đối tượng Resource
				File targetDir = resource.getFile();
				// Tạo đối tượng File cho tệp tin cụ thể trong thư mục assets/image
				File targetFile = new File(targetDir, file.getOriginalFilename());
				// Chuyển nội dung của tệp tin vào đối tượng File
				file.transferTo(targetFile);
				accSer.update(updateAcc);
				return "redirect:/account/Profile";
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				return "redirect:/account/Profile";
			}
		} else {
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Accounts updateAcc = accSer.findEmail(userDetails.getUsername());
				updateAcc.setName(acc.getName());
				updateAcc.setAddress(DiaChi);
				updateAcc.setPhone(acc.getPhone());
				updateAcc.setGender(exampleRadios);
				accSer.update(updateAcc);
				return "redirect:/account/Profile";
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				return "redirect:/account/Profile";
			}
		}
	}

	@RequestMapping("/account/DangNhap")
	public String login(@RequestParam(name = "returnURL", required = false) String returnURL, Model model,
			@RequestParam(name = "statusLogin", required = false) String statusLogin) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			if (userDetails.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals("ROLE_Cust"))) {

			}
			return "redirect:/account/Profile";
		} else {
			if (returnURL == null) {
				returnURL = "/";
			}
			if (statusLogin == null) {
				statusLogin = "/";
			}
			if (!statusLogin.equals("/")) {
				if (statusLogin.equals("erroEmail")) {
					model.addAttribute("messageWrongPassword", "Mật khẩu hoặc tài khoản không chính xác");
				} else if (statusLogin.equals("erroPass")) {
					model.addAttribute("messageWrongPassword", "Mật khẩu không chính xác");
				} else if (statusLogin.equals("roleManage")) {
					model.addAttribute("messageWrongPassword", "Tài khoản quản lý không thể đăng nhập trang sản phẩm");
				}
			}
			Accounts account = new Accounts();
			model.addAttribute("URL", returnURL);
			model.addAttribute("status", statusLogin);
			model.addAttribute("us", account);
			// Chuyển hướng nếu người dùng chưa đăng nhập
			return "/account/DangNhap";
		}

	}

	@PostMapping("/DangNhap/doLogin")
	public String doLogin(Model model, @ModelAttribute("us") Accounts account,
			@RequestParam(name = "returnURL", required = false) String returnURL,
			@RequestParam(name = "statusLogin", required = false) String statusLogin) {
		try {
			UserDetails detail = usSer.loadUserByUsername(account.getEmail());
			if (detail.getPassword().equals(account.getPassword())) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(detail, null,
						detail.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				if (!detail.getAuthorities().stream()
						.anyMatch(authority -> authority.getAuthority().equals("ROLE_Ad"))) {
					if (!detail.getAuthorities().stream()
							.anyMatch(authority -> authority.getAuthority().equals("ROLE_Staf"))) {
						Accounts loadacc = accSer.findEmail(account.getEmail());
						ForgotPassword forgot = forgotSer.findForgotByAccount(loadacc);
						try {
							if (loadacc.equals(forgot.getAccount())) {
								forgotSer.deleteByAccount(loadacc);
							}
						} catch (Exception e) {
						}
						return "redirect:" + returnURL;
					} else {
						return "redirect:/ChaoMung";
					}
				} else {

					return "redirect:/ChaoMung";
				}
			} else {
				model.addAttribute("URL", returnURL);
				statusLogin = "erroPass";
				model.addAttribute("status", statusLogin);
				return "redirect:/account/DangNhap/?returnURL=" + returnURL + "&statusLogin=" + statusLogin;
			}
		} catch (Exception e) {
			model.addAttribute("URL", returnURL);
			statusLogin = "erroEmail";
			model.addAttribute("status", statusLogin);
			return "redirect:/account/DangNhap/?returnURL=" + returnURL + "&statusLogin=" + statusLogin;

		}

	}

	// oauth2
	@RequestMapping("/oauth2/login/success")
	public String oauth2Success(OAuth2AuthenticationToken oauth2) {
		String email = oauth2.getPrincipal().getAttribute("email");
		String name = oauth2.getPrincipal().getAttribute("name");
		System.out.println(email + "  " + name);
		Accounts acc = new Accounts();
		acc = accSer.findEmail(email);
		if (acc == null) {
			accSer.signupFromOAuth2(oauth2);
		} else {
			accSer.loginFromOAuth2(oauth2);
		}
		return "redirect:/";
	}

	@RequestMapping("/account/DangKy")
	public String singup(Model model) {
		Accounts user = new Accounts();
		model.addAttribute("us", user);
		return "/account/DangKy";
	}

	@PostMapping("/DangKy/doSingup")
	public String doSingup(Model model, @ModelAttribute("us") Accounts us) {
		try {
			if (us.getName() == "" || us.getEmail() == "" || us.getPhone() == "" || us.getPassword() == "") {
				model.addAttribute("message", "Không được để trống thông tin");
				return "/account/DangKy";
			} else {
				Accounts user = accSer.findEmail(us.getEmail());
				if (user == null) {
					Accounts newUser = new Accounts();
					newUser.setName(us.getName());
					newUser.setEmail(us.getEmail());
					newUser.setPhone(us.getPhone());
					newUser.setPassword(us.getPassword());
					newUser.setGender(us.getGender());
					accSer.create(newUser);
					Accounts acc = accSer.findEmail(us.getEmail());
					Role role = roleSer.findRole("Cust");
					Authorities auth = new Authorities();
					auth.setAccount(acc);
					auth.setRole(role);
					authSer.create(auth);
					model.addAttribute("message", "Đang ký thành công");
				} else {
					model.addAttribute("message", "Tài khoản đã được đăng ký");
					return "/account/DangKy";
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return "/account/DangKy";
	}

	@RequestMapping("/DoiMK")
	public String changePassword() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			// Thực hiện chức năng đổi mật khẩu với
			return "/account/DoiMK";
		} else {
			return "redirect:/account/DangNhap/?returnURL=/"; // Chuyển hướng nếu người dùng chưa đăng nhập
		}
	}

	@PostMapping("/DoiMK/changePassword")
	public String doChangePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, Model model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Accounts user = accSer.findEmail(userDetails.getUsername());
			System.out.println(userDetails.getUsername());
			System.out.println(user.getEmail());
			// Kiểm tra xem người dùng đã đăng nhập hay chưa
			if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
				if (user.getPassword().equals(oldPassword)) {
					if (newPassword.equals(confirmPassword)) {
						Accounts us = accSer.findEmail(user.getEmail());
						us.setPassword(newPassword);
						accSer.update(us);
						model.addAttribute("message", "Đổi mật khẩu thành công");
					} else {
						model.addAttribute("message", "Mật khẩu mới và xác nhận mật khẩu phải giống nhau");
					}
					return "/account/DoiMK"; // Chuyển hướng sau khi đổi mật khẩu
				}
				else {
					model.addAttribute("message", "Mật khẩu cũ không chính xác");
				}
			} else {
				return "redirect:/account/dangNhap/?returnURL=/DoiMK"; // Chuyển hướng nếu người dùng chưa đăng nhập
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "/account/DoiMK";
		}
		return "/account/DoiMK";
	}

	@RequestMapping("/account/QuenMK")
	public String forgotPassword() {
		return "account/QuenMK";
	}

	@PostMapping("/QuenMk/forgotPassword")
	public String doForgotPassword(@RequestParam String email, Model model) {
		Accounts acc = new Accounts();
		acc = accSer.findEmail(email);
		if (acc != null) {
			ForgotPassword findforgot = forgotSer.findForgotByAccount(acc);
			if (findforgot == null) {
				String verifyCode = String.valueOf(verifyUtil.generateVerify(8));
				Mailinfo mail = new Mailinfo(null, null, null);
				mail.setFrom("TokoFashion");
				mail.setTo(email);
				String subject = "Khôi phục mật khẩu";
				mail.setSubject(subject);
				mail.setBody("Nhập mã xác thực vào trang web để lấy lại mật khẩu : " + verifyCode);
				ForgotPassword forgotPass = new ForgotPassword();
				forgotPass.setAccount(acc);
				forgotPass.setVerifycode(verifyCode);
				forgotSer.create(forgotPass);
				Accounts account = new Accounts();
				account.setEmail(email);
				session.set("user", account);
				System.out.println("tạo mới");
				try {
					mailimpl.send(mail);
					System.out.println("gửi mail thành công");
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					System.out.println("lỗi gửi mail");
					e.printStackTrace();
				}
				return "redirect:/account/XacThuc";
			} else {
				Mailinfo mail = new Mailinfo(null, null, null);
				mail.setFrom("TokoFashion");
				mail.setTo(email);
				String subject = "Khôi phục mật khẩu";
				mail.setSubject(subject);
				mail.setBody("Nhập mã xác thực vào trang web để lấy lại mật khẩu : " + findforgot.getVerifycode());
				Accounts account = new Accounts();
				account.setEmail(email);
				session.set("user", account);
				System.out.println("ko tạo mới");
				try {
					mailimpl.send(mail);
					System.out.println("gửi mail thành công");
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					System.out.println("lỗi gửi mail");
					e.printStackTrace();
				}
				return "redirect:/account/XacThuc";
			}

		} else {
			model.addAttribute("message",
					"Email của bạn chưa được đăng ký hãy đăng ký tài khoản để tiếp tục sử dụng dịch vụ");
		}
		return "/account/QuenMK";
	}

	@RequestMapping("/account/XacThuc")
	public String verify() {

		try {
			HttpSession getsession = request.getSession();
			Accounts acc = (Accounts) getsession.getAttribute("user");
			Accounts loadAcc = accSer.findEmail(acc.getEmail());
			return "/account/XacThuc";
		} catch (Exception e) {
			return "redirect:/account/DangNhap/?returnURL=/";
		}
	}

	@RequestMapping("/XacThuc/doVerify")
	public String doVerify(@RequestParam String maXT, Model model) {
		HttpSession getsession = request.getSession();
		Accounts acc = (Accounts) getsession.getAttribute("user");
		Accounts loadAcc = accSer.findEmail(acc.getEmail());
		ForgotPassword forgot = forgotSer.findForgotByAccount(loadAcc);
		if (maXT.equals(forgot.getVerifycode())) {
			forgotSer.deleteByAccount(loadAcc);
			return "redirect:/account/KhoiPhucMK";
		} else {
			model.addAttribute("message", "Sai mã xác thực vui lòng kiểm tra lại");
			return "/account/XacThuc";
		}

	}

	@RequestMapping("/account/KhoiPhucMK")
	public String passwordRecovery() {
		try {
			HttpSession getsession = request.getSession();
			Accounts acc = (Accounts) getsession.getAttribute("user");
			Accounts loadAcc = accSer.findEmail(acc.getEmail());
			return "/account/KhoiPhucMK";
		} catch (Exception e) {
			return "redirect:/account/DangNhap/?returnURL=/";
		}
	}

	@PostMapping("/KhoiPhucMK/changePassword")
	public String doPasswordRecovery(@RequestParam String newPassword, Model model) {
		try {
			HttpSession getsession = request.getSession();
			Accounts acc = (Accounts) getsession.getAttribute("user");
			Accounts loadAcc = accSer.findEmail(acc.getEmail());
			loadAcc.setPassword(newPassword);
			accSer.update(loadAcc);
			model.addAttribute("message", "Khôi phục mật khẩu thành công");
			return "/account/KhoiPhucMK";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return "redirect:/account/DangNhap/?returnURL=/";
		}

	}

	@GetMapping("/logout")
	public String logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication + " đã đăng xuất");

		return "redirect:/";
	}
}
