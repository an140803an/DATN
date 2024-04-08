package com.toko.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toko.dao.CategoryDAO;
import com.toko.dao.AccountDAO;
import com.toko.entity.Category;
import com.toko.entity.Accounts;



@Service
public class UserService {

	
	
	AccountDAO dao;

	public List<Accounts> findAll() {
		return dao.findAll();
	}

	public Accounts findById(Integer user) {
		return dao.findById(user).get();
	}

	public Accounts create(Accounts user) {
		return dao.save(user);
	}

	public Accounts update(Accounts user) {
		return dao.save(user);
	}

	public void deleteById(Integer userId) {
		dao.deleteById(userId);
	}

	public boolean existedById(Integer userId) {
		return dao.existsById(userId);
	}
}
