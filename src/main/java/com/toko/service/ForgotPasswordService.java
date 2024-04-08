package com.toko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toko.dao.ForgotPasswordDAO;
import com.toko.entity.Accounts;
import com.toko.entity.ForgotPassword;


@Service
public class ForgotPasswordService {

	@Autowired
	ForgotPasswordDAO dao;
	
	public ForgotPassword create(ForgotPassword fogotPass) {
		return dao.save(fogotPass);
	}
	public ForgotPassword findForgotByAccount(Accounts acc) {
		return dao.findByAccount(acc);
	}
//	public ForgotPassword deleteForgot(ForgotPassword fogotPass) {
//		return dao.delete(fogotPass);;
//	}
	public void deleteByAccount(Accounts account) {
        ForgotPassword forgotPasswords = dao.findByAccount(account);
        dao.delete(forgotPasswords);
    }
}
