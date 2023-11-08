package com.example.aunae_chat.data.documents;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chatMessages")
public class ChatMessage {

    @Transient
    public static final String SEQUENCE_NAME= "message_sequence";

    @Id
    private String id;
    private String messageType;
    private Long chatRoomId;
    private String senderId;
    private String sender;
    private String message;
    private LocalDateTime sendAt;

    private Long idx;

    public static ChatMessage create(
            String id,
            String messageType,
            Long chatRoomId,
            String senderId,
            String sender,
            String message,
            LocalDateTime sendAt
    ) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id = id;
        chatMessage.messageType = messageType;
        chatMessage.chatRoomId = chatRoomId;
        chatMessage.senderId = senderId;
        chatMessage.sender = sender;
        chatMessage.message = message;
        chatMessage.sendAt = sendAt;
        return chatMessage;
    }
}
