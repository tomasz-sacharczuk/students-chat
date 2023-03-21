package com.example.studentschat.controller;

import com.example.studentschat.entity.ChatMessage;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.service.impl.ChatService;
import com.example.studentschat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    UserService userService;
    ChatService chatService;

    @Autowired
    public ChatController(UserService userService, ChatService chatService){
        this.userService = userService;
        this.chatService = chatService;
    }

    @RequestMapping("/chat")
    public ModelAndView chat(ModelAndView modelAndView){
        User user = userService.getCurrentUser();
        String userCredentials = userService.getUserCredentialsWithFirstLetterOfSurname(user);

        modelAndView.setViewName("chat");
        modelAndView.addObject("user",user);
        modelAndView.addObject("userCredentials", userCredentials);
        modelAndView.addObject("chatMessages", chatService.getAllChatMessages());
        modelAndView.addObject("hasAdminRole", userService.getCurrentUser().hasAdminRole());
        return modelAndView;
    }

    @RequestMapping("/chat/deleteMessages")
    public void deleteMessages(ModelAndView modelAndView, HttpServletResponse response) throws IOException {

        chatService.deleteAllMessages();
        response.sendRedirect("/chat");
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        User messageUser = userService.findUserByUsername(chatMessage.getSender());
        if(userService.isUserBanned(messageUser)){
            chatMessage.setType(ChatMessage.MessageType.BANNED);
            return chatMessage;
        }

        chatMessage.setSendTime(LocalDateTime.now());
        chatService.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.banUser")
    @SendTo("/topic/public")
    public ChatMessage banUser(@Payload ChatMessage chatMessage) {
        // banRequest content contains info about {<user>,<banLength>,<credentials>}
        String content = chatMessage.getContent();
        User userToBeBanned = chatService.getUserFromBanRequestMessageContent(content);
        int banLength = chatService.getBanLengthFromBanRequestMessageContent(content);
        chatService.banUserForGivenTime(userToBeBanned, banLength);
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