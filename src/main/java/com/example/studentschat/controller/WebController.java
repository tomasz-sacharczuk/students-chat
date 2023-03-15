package com.example.studentschat.controller;

import com.example.studentschat.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

	@RequestMapping(value="/user_panel", method=RequestMethod.GET)
	public ModelAndView userPanel(ModelAndView mav) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}

		mav.setViewName("user_panel");
		mav.addObject("userName",username);
		return mav;
	}

	@RequestMapping(value="/admin_panel", method=RequestMethod.GET)
	public ModelAndView adminPanel(ModelAndView mav) {
		mav.setViewName("admin_panel");

		return mav;
	}

	@RequestMapping(value="/user_group", method=RequestMethod.GET)
	public ModelAndView userGroup(ModelAndView mav) {
		mav.setViewName("user_group");

		return mav;
	}

	@RequestMapping(value="/groups", method=RequestMethod.GET)
	public ModelAndView groups(ModelAndView mav) {
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
