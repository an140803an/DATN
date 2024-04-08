package com.toko.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toko.dao.CategoryDAO;
import com.toko.dao.AccountDAO;
import com.toko.dao.AuthoritiesDAO;
import com.toko.entity.Accounts;
import com.toko.entity.Authorities;
import com.toko.entity.Category;
import com.toko.entity.Role;




@Service
public class AccountService {

	
	@Autowired
	AccountDAO dao;
	
	@Autowired
	RoleService roleSer;
	
	@Autowired
	AuthoritiesDAO authoritiesDAO;
	
	@Autowired
	AuthorityService authSer;

	@Autowired
	ServletContext context;

	public List<Accounts> findAll() {
		return dao.findAll();
	}

	public Accounts findEmail(String email) {
		return dao.findByEmail(email);
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
	
	public List<Accounts> getListAccount() {
		return dao.getListAccount();
	}

	public Accounts findAccount(int account_id) {
		return dao.findById(account_id).orElse(null);
	}
//	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
//		        String email = oauth2.getPrincipal().getAttribute("email");
//		        String name = oauth2.getPrincipal().getAttribute("name");
//		        String password = Long.toHexString(System.currentTimeMillis());
//
//		        // Create UserDetails
//		        UserDetails user = User.withUsername(email)
//		        		.password(pe.encode(password)).roles("CUSS").build();
//		        // Create Authentication
//		        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//
//		        // Set Authentication in SecurityContextHolder
//		        SecurityContextHolder.getContext().setAuthentication(auth);
//
//		    }
	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
	    String email = oauth2.getPrincipal().getAttribute("email");
	    String name = oauth2.getPrincipal().getAttribute("name");
	    String password = Long.toHexString(System.currentTimeMillis());
	    Accounts acc= dao.findByEmail(email);
	    // Create UserDetails
	    UserDetails user = User.withUsername(email)
        		.password(acc.getPassword()).roles("Cust").build();

	    // Create Authentication
	    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	    // Set Authentication in SecurityContextHolder
	    SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	
	public Accounts saveAccount(Accounts accounts) {
		int id = accounts.getAuthorities().get(0).getAuthorities_id();
		Authorities obj = authoritiesDAO.findById(id).orElse(null);
		obj.setAccount(accounts);
		obj.setRole(accounts.getAuthorities().get(0).getRole());
		authoritiesDAO.saveAndFlush(obj);
		return dao.saveAndFlush(accounts);
	}

	public Accounts addAccount(Accounts accounts) {

		accounts.setCreateDate(new Date());
		dao.saveAndFlush(accounts);
		Authorities obj = new Authorities();
		obj.setAccount(accounts);
		obj.setRole(accounts.getAuthorities().get(0).getRole());
		authoritiesDAO.saveAndFlush(obj);
		return accounts;
	}
	
	public void signupFromOAuth2(OAuth2AuthenticationToken oauth2) {
	    String email = oauth2.getPrincipal().getAttribute("email");
	    String name = oauth2.getPrincipal().getAttribute("name");
	    String password = Long.toHexString(System.currentTimeMillis());
	    Accounts newAcc = new Accounts();
	    newAcc.setEmail(email);
	    newAcc.setName(name);
	    newAcc.setPassword(password);
	    create(newAcc);
	    Accounts acc = dao.findByEmail(email);
		Role role = roleSer.findRole("Cust");
		Authorities authoriti = new Authorities();
		authoriti.setAccount(acc);
		authoriti.setRole(role);
		authSer.create(authoriti);
	    // Create UserDetails
	    UserDetails user = User.withUsername(email)
        		.password(password).roles("Cust").build();

	    // Create Authentication
	    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	    // Set Authentication in SecurityContextHolder
	    SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
