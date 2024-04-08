package com.toko.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toko.entity.Accounts;
import com.toko.entity.Comment;
import com.toko.entity.Product;


public interface CommentDAO extends JpaRepository<Comment, Integer>{
	 @Query("SELECT c FROM Comment c WHERE c.product.productId = :productId")
	    List<Comment> getCommentByProductId(int productId);

	    @Query("SELECT c FROM Comment c WHERE c.product = :product AND c.account = :account")
	    Comment getCommentBy2Id(Product product, Accounts account);
}
