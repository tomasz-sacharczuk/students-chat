package com.example.studentschat.controller;

import com.example.studentschat.component.StatusProvider;
import com.example.studentschat.entity.AlertMessage;
import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.Group;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.GroupRepository;
import com.example.studentschat.repository.UserRepository;
import com.example.studentschat.service.impl.ChangeGroupRequestService;
import com.example.studentschat.service.impl.GroupService;
import com.example.studentschat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class WebController {

	private UserService userService;
	private GroupService groupService;
	private ChangeGroupRequestService changeGroupRequestService;
	private StatusProvider statusProvider;

	@Autowired
	public WebController(UserService userService, GroupService groupService,
						 ChangeGroupRequestService changeGroupRequestService, StatusProvider statusProvider){
		this.userService = userService;
		this.groupService = groupService;
		this.changeGroupRequestService = changeGroupRequestService;
		this.statusProvider = statusProvider;
	}

	@RequestMapping(value="/user_panel", method=RequestMethod.GET)
	public ModelAndView userPanel(ModelAndView mav) {
		User currentUser = userService.getCurrentUser();

		mav.setViewName("user_panel");
		mav.addObject("currentUser",currentUser);
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
	public ModelAndView groups(ModelAndView mav
			,@RequestParam(value="msg", required=false) String msg_code) {
		User currentUser = userService.getCurrentUser();
		mav.addObject("currentUser",currentUser);
		mav.addObject("groups",groupService.getAllGroups());
		if(msg_code!= null) {
			AlertMessage message = new AlertMessage(Integer.parseInt(msg_code));
			mav.addObject("message", message);
		}

		mav.setViewName("groups");

		return mav;
	}

	@RequestMapping(value="/group_changes", method=RequestMethod.GET)
	public ModelAndView groupChanges(ModelAndView mav
			,@RequestParam(value="msg", required=false) String msg_code) {
		User currentUser = userService.getCurrentUser();
		mav.addObject("requestsBy",currentUser.getChangeGroupByRequests());
		mav.addObject("requestsTo",currentUser.getChangeGroupToRequests());
		mav.addObject("requestsForAdmin",changeGroupRequestService.getAllRequestsByStatus(ChangeGroupRequest.STATUS_ACCEPTED_BY_USER));
		if(msg_code!= null) {
			AlertMessage message = new AlertMessage(Integer.parseInt(msg_code));
			mav.addObject("message", message);
		}

		currentUser.getChangeGroupByRequests().forEach(r ->{
			if(r.statusForDelete()){
				changeGroupRequestService.delete(r);
			}
		});

		mav.setViewName("group_changes");

		return mav;
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mav, @RequestParam(name = "error", required = false) Boolean error) {
		if (error != null && error) {
			mav.addObject("error","Niepoprawny login i/lub hasło");
			error = false;
		}
		mav.setViewName("login");
		return mav;
	}


	@RequestMapping(value="/change-request/{id}", method=RequestMethod.POST)
	public void changeRequest(@RequestParam(value="setStatus", required=true) String action
			,@PathVariable String id
			,HttpServletResponse response) throws IOException {

		Integer newStatus = statusProvider.getNewStatusByAction(action);
		Integer messageCode = statusProvider.getMessageCodeByAction(action);

		ChangeGroupRequest request = changeGroupRequestService.getRequestById(Long.parseLong(id));
		if(newStatus == ChangeGroupRequest.STATUS_ACCEPTED_BY_ADMIN_INFO){
			request = changeGroupRequestService.changeGroups(request);
		}

		if(newStatus != null){
			request.setStatus(newStatus);
			changeGroupRequestService.saveRequest(request);
		}
		response.sendRedirect("/group_changes?msg="+messageCode);
	}

	@RequestMapping(value="/create-request/{id}", method=RequestMethod.POST)
	public void createRequest(@PathVariable String id
			,HttpServletResponse response) throws IOException {

		User userBy = userService.getCurrentUser();
		User userTo = userService.getUserById(Long.parseLong(id));

		if(userTo.anyActiveRequestExists() || userBy.anyActiveRequestExists()){
			response.sendRedirect("/groups?msg="+AlertMessage.CREATE_REQUEST_FAILED_MESSAGE_CODE);
		} else {

			ChangeGroupRequest changeGroupRequest = new ChangeGroupRequest(userBy, userTo);

			changeGroupRequestService.save(changeGroupRequest);

			userBy.getChangeGroupByRequests().add(changeGroupRequest);
			userTo.getChangeGroupToRequests().add(changeGroupRequest);

			userService.saveUser(userBy);
			userService.saveUser(userTo);

			response.sendRedirect("/groups?msg=" + AlertMessage.CREATE_REQUEST_SUCCESS_MESSAGE_CODE);
		}
	}

}