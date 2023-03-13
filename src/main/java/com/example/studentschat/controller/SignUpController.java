package com.example.studentschat.controller;

import com.example.studentschat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentschat.service.SignUpService;


@RestController
public class SignUpController {

	private SignUpService signUpService;
	
	@Autowired
	public SignUpController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping("/api/sign_up")
	public String signUp(String username, String password) {
		User userToSignUp = new User(username, password);
		signUpService.signUpUser(userToSignUp);
		return "User signed up";
	}

}
