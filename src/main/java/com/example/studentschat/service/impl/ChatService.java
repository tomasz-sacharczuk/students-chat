package com.example.studentschat.service.impl;

import com.example.studentschat.entity.ChatMessage;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.ChatRepository;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserService userService;

    public List<ChatMessage> getAllChatMessages(){
        List<ChatMessage> chatMessagesList = chatRepository.findAllByOrderBySendTime();
        if (chatMessagesList.isEmpty()) return Collections.emptyList();
        List<ChatMessage> chatMessagesListWithCredentials = setCredentialsForEachMessage(chatMessagesList);
        return chatMessagesListWithCredentials;
    }

    public List<ChatMessage> setCredentialsForEachMessage(List<ChatMessage> messages){
        for (ChatMessage message : messages){
            User messageUser = userService.findUserByUsername(message.getSender());
            message.setCredentials(userService.getUserCredentialsWithFirstLetterOfSurname(messageUser));
        }
        return messages;
    }

    public void save(ChatMessage chatMessage){
        chatRepository.save(chatMessage);
    }

    public User getUserFromBanRequestMessageContent(String content){
        String[] contentList = getDataOfBanRequestContent(content);
        return userService.findUserByUsername(contentList[0]);
    }

    public int getBanLengthFromBanRequestMessageContent(String content){
        String[] contentList = getDataOfBanRequestContent(content);
        return Integer.parseInt(contentList[1]);
    }

    private String[] getDataOfBanRequestContent(String content){
        String[] contentList = content.split(",");
        if (contentList.length != 3) {
            throw new IllegalArgumentException("Wrong format of string - exptected xxx,xxx,xxx");
        }
        return contentList;
    }

    public void banUserForGivenTime(User userToBeBanned, int timeLengthInMinutes){
        LocalDateTime now = LocalDateTime.now();
        userToBeBanned.setChatBanEndTime(now.plusMinutes(timeLengthInMinutes));
        userService.saveUser(userToBeBanned);
    }

    public void deleteAllMessages() {
        chatRepository.deleteAll();
    }
}
