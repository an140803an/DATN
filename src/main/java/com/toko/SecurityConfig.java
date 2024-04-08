package com.toko;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Autowired
	public UserDetailsService userDetailsService;
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .cors().disable().csrf().disable()
	        .authorizeRequests()	     
            .antMatchers("/quan-li-tai-khoan","/bao-cao-doanh-thu").hasAuthority("ROLE_Ad")
            .antMatchers("/ChaoMung","/quan-li-san-pham","/themSP","/quan-li-danh-muc","/themDM","/quan-li-don-hang","/QLDonhang/edit").hasAnyAuthority("ROLE_Ad", "ROLE_Staf")
            .and()
            .exceptionHandling()
            .accessDeniedPage("/")
	        .and()
	        .formLogin()
	            .loginPage("/account/DangNhap")
	            .loginProcessingUrl("/account/DangNhap/dologin")
	            .defaultSuccessUrl("/", false)
	            .failureUrl("/account/DangNhap")
	        .and()
	        .exceptionHandling().accessDeniedPage("/security/access/denied")
	        .and()
	        .logout()
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .addLogoutHandler(new SecurityContextLogoutHandler())
	            .clearAuthentication(true)
	        .and()
	        .oauth2Login()
	            .loginPage("/account/DangNhap")
	            .defaultSuccessUrl("/oauth2/login/success", true)
	            .failureUrl("/account/DangNhap")
	            .authorizationEndpoint()
	                .baseUri("/oauth2/authorization");
	}

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/rsx/**", "/api/**");
	}
}