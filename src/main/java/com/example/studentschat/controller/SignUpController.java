package com.example.studentschat.controller;

import com.example.studentschat.entity.user.Role;
import com.example.studentschat.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentschat.service.SignUpService;

import java.util.HashSet;
import java.util.Set;


@RestController
public class SignUpController {

	private SignUpService signUpService;
	
	@Autowired
	public SignUpController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping("/api/sign_up")
	public String signUp(String username, String password) {
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(new Role("USER"));
		User userToSignUp = new User(username, password, userRoles);
		signUpService.signUpUser(userToSignUp);
		return "User signed up";
	}

}
