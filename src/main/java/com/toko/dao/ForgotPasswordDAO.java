package com.toko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toko.entity.Accounts;
import com.toko.entity.ForgotPassword;






@Repository
public interface ForgotPasswordDAO extends JpaRepository<ForgotPassword, Integer>{
ForgotPassword findByAccount(Accounts account);

}
