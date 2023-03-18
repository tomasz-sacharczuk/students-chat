package com.example.studentschat.repository;

import com.example.studentschat.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByOrderBySendTime();
}
