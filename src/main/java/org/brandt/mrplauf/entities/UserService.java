package org.brandt.mrplauf.entities;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//List<User> l = new ArrayList();
		User s = new User();
		s.username = "Eric";
		s.password = "abc123";
		s.enabled = true;
		return s;
	}
	
	

}
