package com.toko.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toko.dao.RoleDAO;
import com.toko.entity.Role;

@Service
public class RoleService {
	@Autowired
	RoleDAO dao;
	
	public Role findRole(String role) {
		 Optional<Role> optionalRole = dao.findById(role);
		 if (optionalRole.isPresent()) {
		        return optionalRole.get();
		    } else {
		        // Xử lý trường hợp không tìm thấy Role với id cụ thể
		        return null; // hoặc ném một exception phù hợp
		    }
	}

}
