package com.toko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toko.entity.Category;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
    // Các phương thức tương tác với cơ sở dữ liệu
    // Ví dụ:
    
    // Tìm category theo type
    Category findByType(String type);
    
    // Các phương thức khác tùy theo nhu cầu của ứng dụng
}