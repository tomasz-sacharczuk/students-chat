package com.example.studentschat.controller;

import com.example.studentschat.entity.Group;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.service.impl.GroupService;
import com.example.studentschat.service.impl.SignUpService;
import com.example.studentschat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class SignUpController {

	private SignUpService signUpService;
	private GroupService groupService;
	private UserService userService;

	@Autowired
	public SignUpController(SignUpService signUpService, GroupService groupService, UserService userService) {
		this.groupService = groupService;
		this.signUpService = signUpService;
		this.userService = userService;
	}

	@RequestMapping(value = "/signUpForm")
	public ModelAndView userForm(ModelAndView modelAndView) {
		modelAndView.addObject("user", new User());
		modelAndView.addObject("groups", groupService.getAllGroups());
		modelAndView.setViewName("signUpForm");
		return modelAndView;
	}

	@RequestMapping(value = "/signUpUser", method = RequestMethod.POST)
	public ModelAndView singUpUser(@Valid User user, BindingResult bindingResult,
								   ModelAndView modelAndView, @Valid Long groupId) {
		Group potentialGroup = groupService.getGroupById(groupId);
		List<Object> otherErrors = new ArrayList<>();

		if (potentialGroup != null &&
				potentialGroup.getGroupUsers().size() >= potentialGroup.getLimit()){
			modelAndView.addObject(
					"groupLimitReachedError","Osiągnięto limit grupy");
			otherErrors.add("groupLimitReachedError");
		}

		if (user.getUsername() != null &&
				userService.findOptionalUserByUsername(user.getUsername()).isPresent()){
			modelAndView.addObject(
					"nonUniqueUsernameError","Istnieje już użytkownik z podanym loginem");;
			otherErrors.add("nonUniqueUsernameError");
		}

		if (bindingResult.hasErrors() || !otherErrors.isEmpty()) {
			modelAndView.addObject("groups", groupService.getAllGroups());
			modelAndView.setViewName("signUpForm");
		} else {
			user.setGroup(groupService.getGroupById(groupId));
			signUpService.signUpUser(user);
			modelAndView.setViewName("user_panel");
		}
		return modelAndView;

	}
}
