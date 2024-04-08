package com.toko.rest.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toko.entity.Accounts;
import com.toko.service.AccountService;

import net.coobird.thumbnailator.Thumbnails;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/account")
public class AccountRestController {
	@Autowired
	AccountService service;

	@GetMapping("/currentUser")
	public Accounts getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			Accounts account = service.findEmail(userDetails.getUsername());
			return account;

		}
		return null;
	}

	@GetMapping("/get-list-account")
	public List<Accounts> getlistAccount() {
		return service.getListAccount();
	}

	@PostMapping("/find-account/{account_id}")
	public Accounts findAccount(@PathVariable int account_id) {
		return service.findAccount(account_id);
	}

	@PostMapping("/save-account")
	public Accounts saveAccount(@RequestBody Accounts accounts) {
		return service.saveAccount(accounts);
	}

	@PostMapping("/add-account")
	public Accounts addAccount(@RequestBody Accounts accounts) {
		System.out.println("accounts: " + accounts);
		return service.addAccount(accounts);
	}

	@PostMapping("/save-image")
	public String saveImg(@RequestParam("photoFiles") MultipartFile[] photoFiles) {
		String currentDir = System.getProperty("user.dir");
		System.out.println("Thu muc hien tai: " + currentDir);
		// Lưu hình ảnh vào thư mục static/images
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					String originalFileName = photoFile.getOriginalFilename();
					String newFileName = originalFileName;
					String pathUpload = currentDir + "/src/main/resources/static/assetsAdmin/img/" + newFileName;
					System.out.println("pathUpload: " + pathUpload);
					try {
						photoFile.transferTo(new File(pathUpload));
						String contentType = photoFile.getContentType();
						boolean type = true;
						if (contentType.startsWith("image")) {

						} else if (contentType.startsWith("video")) {
							type = false;
						}
						if (type == true) {
							long fileSize = photoFile.getSize();
							if (fileSize > 1 * 1024 * 1024) {
								double quality = 0.6;
								String outputPath = pathUpload;
								Thumbnails.of(pathUpload).scale(1.0).outputQuality(quality).toFile(outputPath);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		return "success";
	}

}