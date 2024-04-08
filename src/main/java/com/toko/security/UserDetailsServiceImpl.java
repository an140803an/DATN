package com.toko.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toko.entity.Accounts;
import com.toko.service.AccountService;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	AccountService accSer;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Accounts user = accSer.findEmail(username);
			return new UserDetailsImpl(user);
		} catch (UsernameNotFoundException e) {
			System.out.println("loi");
			throw new UsernameNotFoundException("User not found");
		}
	}

}
