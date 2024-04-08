package com.toko.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.toko.entity.Accounts;

import lombok.Getter;

public class UserDetailsImpl implements UserDetails{
	
	@Getter 
	private Accounts user;
	public UserDetailsImpl(Accounts user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return user.getAuthorities().stream().map(a -> {
			return new SimpleGrantedAuthority("ROLE_" + a.getRole().getId());
		}).toList();
		}
	
	@Override 
	public String getPassword() {
		
		return user.getPassword();
	}

	
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
