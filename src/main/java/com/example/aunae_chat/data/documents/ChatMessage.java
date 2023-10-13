package com.example.aunae_chat.data.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatMessages")
public class ChatMessage {
    @Id
    private String id;
    private String messageType;
    private Long chatRoomId;
    private String senderId;
    private String sender;
    private String message;
}
