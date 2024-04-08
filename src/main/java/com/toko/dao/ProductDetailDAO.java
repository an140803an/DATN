package com.toko.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toko.entity.ProductDetail;

@Repository
public interface ProductDetailDAO extends JpaRepository<ProductDetail, Integer>{
//	@Query("SELECT DISTINCT pd.size FROM ProductsDetails pd WHERE pd.productID.productId = ?1")
//    List<String> findSizesByProductId(String id);
	@Query("SELECT pd FROM ProductDetail pd WHERE pd.product.productId = ?1")
    List<ProductDetail> findByProductId(Integer productId);
}
