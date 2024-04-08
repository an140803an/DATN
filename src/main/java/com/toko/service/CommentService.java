package com.toko.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toko.dao.CommentDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Comment;
import com.toko.entity.Product;

@Service
public class CommentService {
	 @Autowired
	    CommentDAO commentDAO;

	    public List<Comment> getComment(int productId) {
	        return commentDAO.getCommentByProductId(productId);
	    }

	    public Comment addComment(Product product, Accounts account, String content) {
	        Comment o = commentDAO.getCommentBy2Id(product, account);
	        if (o != null) {
	            o.setContent(content);
	            o.setCommentDate(new Date());
	            commentDAO.saveAndFlush(o);
	            return o;
	        }
	        Comment obj = new Comment();
	        obj.setAccount(account);
	        obj.setCommentDate(new Date());
	        obj.setProduct(product);
	        obj.setContent(content);
	        return commentDAO.saveAndFlush(obj);
	    }
}
