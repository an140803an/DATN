package com.toko.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toko.entity.Category;
import com.toko.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.category.id = 1")
	List<Product> findAllStartingWithAoThun();

	@Query("SELECT p FROM Product p WHERE p.category.id = 3")
	List<Product> findAllStartingWithAoPolo();

	@Query("SELECT p FROM Product p WHERE p.category.id = 2")
	List<Product> findAllStartingWithAoBabyTee();

	@Query("SELECT p FROM Product p WHERE p.category.id = 4")
	List<Product> findAllStartingWithAoSoMi();

	@Query("SELECT p FROM Product p WHERE p.category.id = 5")
	List<Product> findAllStartingWithAoKhoac();

	@Query("SELECT p FROM Product p WHERE p.category.id = 6")
	List<Product> findAllStartingWithAoHoodie();

	@Query("SELECT p FROM Product p WHERE p.category.id = 7")
	List<Product> findAllStartingWithQuan();

	@Query("SELECT p FROM Product p WHERE p.category.id = 8")
	List<Product> findAllStartingWithPhuKien();

	@Query("select p from Product p where p.category.categoryId =:id")
	List<Product> findProductByIdCategory(@Param("id") int id);

	@Query("SELECT o FROM Product o WHERE o.productId=:productId ")
	Product findProductById(@Param("productId") Integer productId);

	@Query("SELECT p FROM Product p WHERE p.category.id=?1")
	List<Product> findByCategoryId(Integer id);

//	Low Discount
	// Sắp xếp product từ giá thấp đến cao All
	@Query("SELECT p FROM Product p ORDER BY p.price - (p.price * p.discount / 100) ")
	List<Product> findBySortPriceLowAll();

	// Sắp xếp product từ A --> Z Category
	@Query("SELECT p FROM Product p WHERE p.category = :categoryId ORDER BY p.price - (p.price * p.discount / 100) ")
	List<Product> findBySortPriceLowCategory(@Param("categoryId") Category cate);

// High Discount
	// Sắp xếp product từ giá cao đến thấp All
	@Query("SELECT p FROM Product p ORDER BY p.price  - (p.price * p.discount / 100) DESC")
	List<Product> findBySortPriceHighAll();

	// Sắp xếp product từ giá cao đến thấp Z Category
	@Query("SELECT p FROM Product p WHERE p.category = :categoryId ORDER BY p.price  - (p.price * p.discount / 100) DESC")
	List<Product> findBySortPriceHighCategory(@Param("categoryId") Category cate);

// Dưới 200.000đ
//			//Tìm product theo giá 0 đến 200.000 All
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) BETWEEN 0 AND 200000 ")
	List<Product> findBySortPriceBetween02All();

	// Tìm product theo giá 0 đến 200.000 Category
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) BETWEEN 0 AND 200000  AND p.category = :categoryId ")
	List<Product> findBySortPriceBetween02Category(@Param("categoryId") Category cate);

// Từ 200 => đến 500đ			
	// tìm product có theo giá đã áp dụng mã giảm giá từ 200 đến 500 All
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) BETWEEN 200000 AND 500000 ")
	List<Product> findBySortPriceBetween25All();

	// tìm product có theo giá đã áp dụng mã giảm giá từ 200 đến 500 All
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) BETWEEN 200000 AND 500000  AND p.category = :categoryId ")
	List<Product> findBySortPriceBetween25Category(@Param("categoryId") Category cate);

// Từ 500đ trở lên	
	// tìm product có theo giá đã áp dụng mã giảm giá từ 200 đến 500 All
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) > 500000 ")
	List<Product> findBySortPriceBetween5upAll();

	// tìm product có theo giá đã áp dụng mã giảm giá từ 200 đến 500 All
	@Query("SELECT p FROM Product p WHERE p.price - (p.price * p.discount / 100) > 500000  AND p.category = :categoryId ")
	List<Product> findBySortPriceBetween5upCategory(@Param("categoryId") Category cate);

	// Sắp xếp product từ A --> Z moi
	@Query("SELECT o FROM Product o ORDER BY o.name")
	List<Product> findBySortNameLowAll();

	// Sắp xếp product từ A --> Z Category
	@Query("SELECT p FROM Product p WHERE p.category = :categoryId ORDER BY p.name")
	List<Product> findBySortNameLowCategory(@Param("categoryId") Category cate);

	// Sắp xếp product từ A --> Z moi
	@Query("SELECT o FROM Product o ORDER BY o.name DESC")
	List<Product> findBySortNameHighAll();

	// Sắp xếp product từ A --> Z Category
	@Query("SELECT p FROM Product p WHERE p.category = :categoryId ORDER BY p.name DESC")
	List<Product> findBySortNameHighCategory(@Param("categoryId") Category cate);

//Tìm kiếm sản phẩm		
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% ")
	List<Product> findByName(String keyword);

}
