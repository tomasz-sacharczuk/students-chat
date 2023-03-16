package com.example.studentschat.controller;

import com.example.studentschat.entity.ChatMessage;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatController {

    UserService userService;

    @Autowired
    public ChatController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/chat")
    public ModelAndView chat(ModelAndView modelAndView){
        User user = userService.getCurrentUser();
        String userCredentials = userService.getUserCredentialsWithFirstLetterOfSurname(user);

        modelAndView.setViewName("chat");
        modelAndView.addObject("user",user);
        modelAndView.addObject("userCredentials",userCredentials);
        return modelAndView;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}