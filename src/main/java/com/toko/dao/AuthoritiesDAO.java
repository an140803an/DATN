package com.toko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toko.entity.Accounts;
import com.toko.entity.Authorities;




@Repository
public interface AuthoritiesDAO extends JpaRepository<Authorities, Integer>{
	
	

	Authorities findByAccount(Accounts acc);
}
