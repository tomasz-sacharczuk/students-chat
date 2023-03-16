package com.example.studentschat.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private LocalDateTime sendTime;
    private String sender; //TODO mapping

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}