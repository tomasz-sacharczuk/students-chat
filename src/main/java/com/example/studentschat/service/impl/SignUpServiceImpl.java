package com.example.studentschat.service.impl;

import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.UserRepository;
import com.example.studentschat.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SignUpServiceImpl implements SignUpService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public SignUpServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User signUpUser(User user) {
		Assert.isNull(user.getId(), "Can't sign up given user, it already has set id. User: " + user.toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(user);
		return savedUser;
	}

}
