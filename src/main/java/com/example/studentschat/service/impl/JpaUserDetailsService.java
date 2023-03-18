package com.example.studentschat.service.impl;

import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> usernameOptional = userRepository.findByUsername(username);
		if(!usernameOptional.isPresent()) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		User user = usernameOptional.get();
		return user;
	}

}
