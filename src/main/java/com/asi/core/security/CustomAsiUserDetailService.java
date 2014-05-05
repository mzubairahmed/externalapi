package com.asi.core.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAsiUserDetailService implements UserDetailsService {

	
	@Override
	public UserDetails loadUserByUsername(final String userName)
			throws UsernameNotFoundException {
		List<GrantedAuthority> gr = new ArrayList<GrantedAuthority>();
		SimpleGrantedAuthority grau = new SimpleGrantedAuthority("ROLE_CUSTOMER");
		gr.add(grau);
		UserDetails user = new User(userName, "password", true, true, true, true, gr);
		return user;
	}

}
