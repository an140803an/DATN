package com.toko.controller;

import java.io.File;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toko.entity.Accounts;
import com.toko.service.AccountService;

import net.coobird.thumbnailator.Thumbnails;

@RestController
@CrossOrigin("*")
public class QLTKController {

    @Autowired
    AccountService accountService;

    @GetMapping("/get-list-account")
    public List<Accounts> getlistAccount() {
        return accountService.getListAccount();
    }

    @PostMapping("/find-account/{account_id}")
    public Accounts findAccount(@PathVariable int account_id) {
        return accountService.findAccount(account_id);
    }

    @PostMapping("/save-account")
    public Accounts saveAccount(@RequestBody Accounts accounts) {
        return accountService.saveAccount(accounts);
    }

    @PostMapping("/add-account")
    public Accounts addAccount(@RequestBody Accounts accounts) {
        System.out.println("accounts: " + accounts);
        return accountService.addAccount(accounts);
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
