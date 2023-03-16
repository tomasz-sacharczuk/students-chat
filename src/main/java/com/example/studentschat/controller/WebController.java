package com.example.studentschat.controller;

import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.GroupRepository;
import com.example.studentschat.repository.UserRepository;
import com.example.studentschat.service.impl.GroupService;
import com.example.studentschat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class WebController {

	private UserService userService;
	private GroupService groupService;

	@Autowired
	public WebController(UserService userService, GroupService groupService){
		this.userService = userService;
		this.groupService = groupService;
	}

	@RequestMapping(value="/user_panel", method=RequestMethod.GET)
	public ModelAndView userPanel(ModelAndView mav) {
		User currentUser = userService.getCurrentUser();

		mav.setViewName("user_panel");
		mav.addObject("userName",currentUser.getUsername());
		return mav;
	}

	@RequestMapping(value="/admin_panel", method=RequestMethod.GET)
	public ModelAndView adminPanel(ModelAndView mav) {
		mav.setViewName("admin_panel");

		return mav;
	}

	@RequestMapping(value="/user_group", method=RequestMethod.GET)
	public ModelAndView userGroup(ModelAndView mav) {

		User currentUser = userService.getCurrentUser();
		mav.addObject("currentUser",currentUser);

		mav.addObject("group",currentUser.getGroup());
		mav.setViewName("user_group");

		return mav;
	}


	@RequestMapping(value="/groups", method=RequestMethod.GET)
	public ModelAndView groups(ModelAndView mav) {
		User currentUser = userService.getCurrentUser();
		mav.addObject("currentUser",currentUser);

		mav.addObject("groups",groupService.getAllGroups());

		mav.setViewName("groups");

		return mav;
	}

	@RequestMapping(value="/group_changes", method=RequestMethod.GET)
	public ModelAndView groupChanges(ModelAndView mav) {
		mav.setViewName("group_changes");

		return mav;
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mav) {
		mav.setViewName("login");

		return mav;
	}

}