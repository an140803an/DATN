package com.toko.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class SaveFileService {

	  private final ResourceLoader resourceLoader;

	    public SaveFileService(ResourceLoader resourceLoader) {
	        this.resourceLoader = resourceLoader;
	    }

	    public void saveFile(MultipartFile file) throws IOException {
	    	try {
	    		  // Lấy đối tượng Resource từ đường dẫn tương đối
		        Resource resource = resourceLoader.getResource("classpath:static/");

		        // Lấy File tương ứng với đối tượng Resource
		        File staticDir = resource.getFile();

		        // Tạo đối tượng File cho tệp tin cụ thể trong thư mục assets/image
		        File targetFile = new File(staticDir, "assets/image/" + file.getOriginalFilename());

		        // Chuyển nội dung của tệp tin vào đối tượng File
		        file.transferTo(targetFile);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
	      
	    }
}
