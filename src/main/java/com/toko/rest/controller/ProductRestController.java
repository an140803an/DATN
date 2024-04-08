package com.toko.rest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toko.entity.Product;
import com.toko.service.ProductDetailService;
import com.toko.service.ProductService;

@CrossOrigin("*")
@RestController
public class ProductRestController {
	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailService detailService;


	
	@GetMapping("/rest/products")
	public List<Product> getAll() {
		return productService.findAll();
	}

//	
//	@GetMapping("/rest/products/{id}")
//	public Product getAllWithDetail(@PathVariable("id") Integer id){
//		return productService.findProductWithDetails(id);
//	}
//	
	@GetMapping("/rest/products/{id}")
	public Product getOne(@PathVariable("id") Integer id) {
		return productService.findById(id);
	}

	@PostMapping("/rest/products")
	public Product create(@RequestBody Product product) {
		productService.create(product);
		return product;
	}

	@PutMapping("/rest/products/{id}")
	public Product update(@RequestBody Product product, @PathVariable("id") Integer id) {
		if (productService.existedById(id)) {
			productService.update(product);
		} else {
			throw new RuntimeException();
		}
		return product;
	}

//	@DeleteMapping("/rest/products/{id}")
//	public void delete(@PathVariable("id") Integer id) {
//		productService.deleteById(id);
//	}
	
	@PutMapping("/rest/products/{id}/disable")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
	    // Tìm sản phẩm trong cơ sở dữ liệu
	    Product product = productService.findById(id);

	    if (product != null) {
	        // Cập nhật trạng thái của sản phẩm thành 'Hết hàng'
	        product.setStatus("The product is out of stock");
	        
	        // Lưu trạng thái cập nhật vào cơ sở dữ liệu
	        productService.update(product);

	        return ResponseEntity.ok().body("Product status updated to 'Hết hàng' successfully.");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	 // Phương thức tìm kiếm sản phẩm theo ID
//	 @GetMapping("/{id}")
//	    public Product getProductById(@PathVariable("id") Integer id) {
//	        Product product = productService.findById(id);
//			return product;
//
//	      
//	    }
	 @GetMapping("/{id}")
	 public List<Product> getProductsById(@PathVariable("id") Integer id) {
	     List<Product> products = productService.findByCategoryId(id);
	     return products;
	 }
	 
	 @PostMapping("/rest/products/upload")
	    public ResponseEntity<String> uploadImage(@RequestParam(value = "file") MultipartFile file) {
	        // Check if the file is empty
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body("Please select a file to upload");
	        }
	        
	        try {
	            // Save the file to a predefined location on the server
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get("static/assets/image/" + file.getOriginalFilename());
	            Files.write(path, bytes);
	            
	            return ResponseEntity.ok().body("File uploaded successfully");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
	        }
	    }
	 
//	 @PostMapping("/upload")
//	 public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//	     try {
//	         // Check if the file is empty
//	         if (file.isEmpty()) {
//	             return ResponseEntity.badRequest().body("Please select a file to upload");
//	         }
//
//	         // Check file type
//	         String contentType = file.getContentType();
//	         if (!contentType.startsWith("image/")) {
//	             return ResponseEntity.badRequest().body("Invalid file type. Please upload an image file");
//	         }
//
//	         // Generate a unique file name to avoid conflicts
//	         String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//	         // Save the file to a predefined location on the server
//	         byte[] bytes = file.getBytes();
//	         Path path = Paths.get("static/assets/image/" + fileName);
//	         Files.write(path, bytes);
//
//	         return ResponseEntity.ok().body("File uploaded successfully");
//	     } catch (IOException e) {
//	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
//	     }
//	 }
	 

}
