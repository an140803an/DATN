package com.toko.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toko.entity.Accounts;

@Repository

public interface AccountDAO extends JpaRepository<Accounts, Integer> {
//    // Các phương thức tương tác với cơ sở dữ liệu
//    // Ví dụ:
//    
//    // Tìm user theo username
//    User findByUsername(String username);
//    
//    // Tìm user theo email
//    User findByEmail(String email);
//    
//    // Các phương thức khác tùy theo nhu cầu của ứng dụng
	Accounts findByEmail(String email);
	
	@Query("SELECT o FROM  Accounts o ")
	List<Accounts> getListAccount();
}
