package com.example.studentschat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ChatMessage {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private MessageType type;
    private String content;
    private LocalDateTime sendTime;
    private String sender;
    @Transient
    private String credentials; // transient field for view purposes

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        BANNED,
        BAN_REQUEST
    }
}