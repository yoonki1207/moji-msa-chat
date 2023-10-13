package com.example.aunae_chat.repository;

import com.example.aunae_chat.data.documents.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
