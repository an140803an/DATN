package com.toko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toko.dao.AuthoritiesDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Authorities;


@Service
public class AuthorityService {

	@Autowired
	AuthoritiesDAO dao;
	
	public Authorities create(Authorities auth) {
		return dao.save(auth);
	}
	
	public Authorities findByAccount(Accounts Acc) {
		return dao.findByAccount(Acc);
	}
	
	
}
