package com.asi.core.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:users.properties")
public class CustomAsiUserDetailService implements UserDetailsService {

	@Resource
	private Environment environment;
	@Override
	public UserDetails loadUserByUsername(final String userName)
			throws UsernameNotFoundException {
		String userPassword = environment.getProperty(String.format("user.%s.pass",userName));
		String userRole = environment.getProperty(String.format("user.%s.role",userName));
		List<GrantedAuthority> gr = new ArrayList<GrantedAuthority>();
		SimpleGrantedAuthority grau = new SimpleGrantedAuthority(userRole);
		gr.add(grau);
		UserDetails user = new User(userName, userPassword, true, true, true, true, gr);
		return user;
	}

}
