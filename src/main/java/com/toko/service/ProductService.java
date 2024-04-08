package com.toko.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.toko.dao.ProductDAO;
import com.toko.entity.Category;
import com.toko.entity.Product;

@Service
public class ProductService {
	@Autowired
	ProductDAO dao;

	public List<Product> findAll() {
		List<Product> list = dao.findAll();
		return list;
	}

	public Product findById(Integer id) {
		Product product = dao.findById(id).get();
		return product;
	}

	public List<Product> findByCategoryId(Integer id) {
		List<Product> list = dao.findByCategoryId(id);
		return list;
	}


	public Product create(Product product) {
		return dao.save(product);
	}

	public boolean existedById(Integer id) {
		return dao.existsById(id);
	}

	public Product update(Product product) {
		return dao.save(product);
	}

	public void deleteById(Integer id) {
		dao.deleteById(id);
	}
	

	  public Page<Product> findPaginated(Pageable pageable,List<Product> pro) {
	        int pageSize = pageable.getPageSize();
	        int currentPage = pageable.getPageNumber();
	        int startItem = currentPage * pageSize;
	        List<Product> list;
	        if (pro.size() < startItem) {
	            list = Collections.emptyList();
	        } else {
	            int toIndex = Math.min(startItem + pageSize, pro.size());
	            list = pro.subList(startItem, toIndex);
	        }
	        Page<Product> bookPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), pro.size());
	        return bookPage;
	    }
//	public Page<Product> getPaginatedProducts(int page, int pageSize) {
//        // Create a PageRequest object for pagination
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//
//        // Fetch paginated data from the repository
//        return dao.findAll(pageRequest);
//    }
	  
	  //CategoryList
	  public List<Product> findAllStartingWithAoThun(){
		  return dao.findAllStartingWithAoThun();
	  }
	  public List<Product> findAllStartingWithAoPolo(){
		  return dao.findAllStartingWithAoPolo();
	  }
	  public List<Product> findAllStartingWithAoBabyTee(){
		  return dao.findAllStartingWithAoBabyTee();
	  }
	  public List<Product> findAllStartingWithAoSoMi(){
		  return dao.findAllStartingWithAoSoMi();
	  }
	  public List<Product> findAllStartingWithAoKhoac(){
		  return dao.findAllStartingWithAoKhoac();
	  }
	  public List<Product> findAllStartingWithAoHoodie(){
		  return dao.findAllStartingWithAoHoodie();
	  }
	  public List<Product> findAllStartingWithQuan(){
		  return dao.findAllStartingWithQuan();
	  }
	  public List<Product> findAllStartingWithPhuKien(){
		  return dao.findAllStartingWithPhuKien();
	  }
	  //Filter
	  public List<Product> findBySortPriceLowAll(){
		  return dao.findBySortPriceLowAll();
	  }
	  public List<Product> findBySortPriceLowCategory(@Param("categoryId") Category cate){
		  return dao.findBySortPriceLowCategory(cate);
	  }
	  public List<Product> findBySortPriceHighAll(){
		  return dao.findBySortPriceHighAll();
	  }
	  public List<Product> findBySortPriceHighCategory(@Param("categoryId") Category cate){
		  return dao.findBySortPriceHighCategory(cate);
	  }
	  public List<Product> findBySortPriceBetween02All(){
		  return dao.findBySortPriceBetween02All();
	  }
	  public List<Product> findBySortPriceBetween02Category(@Param("categoryId") Category cate){
		  return dao.findBySortPriceBetween02Category(cate);
	  }
	  public List<Product> findBySortPriceBetween25All(){
		  return dao.findBySortPriceBetween25All();
	  }
	  public List<Product> findBySortPriceBetween25Category(@Param("categoryId") Category cate){
		  return dao.findBySortPriceBetween25Category(cate);
	  }
	  public List<Product> findBySortPriceBetween5upAll(){
		  return dao.findBySortPriceBetween5upAll();
	  }
	  public List<Product> findBySortPriceBetween5upCategory(@Param("categoryId") Category cate){
		  return dao.findBySortPriceBetween5upCategory(cate);
	  }
	  public List<Product> findBySortNameLowAll(){
		  return dao.findBySortNameLowAll();
	  }
	  public List<Product> findBySortNameLowCategory(@Param("categoryId") Category cate){
		  return dao.findBySortNameLowCategory(cate);
	  }
	  public List<Product> findBySortNameHighAll(){
		  return dao.findBySortNameHighAll();
	  }
	  public List<Product> findBySortNameHighCategory(@Param("categoryId") Category cate){
		  return dao.findBySortNameHighCategory(cate);
	  }
	  //tìm kiếm sản phẩm
	  public List<Product> findByName(String keyword){
	        return dao.findByName("%" + keyword + "%");
	    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
}
