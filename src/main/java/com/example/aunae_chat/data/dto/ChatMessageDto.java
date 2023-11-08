package com.example.aunae_chat.data.dto;

import com.example.aunae_chat.data.documents.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, LEAVE, MEDIA, NOTICE
    }
    private String id;
    private Long idx;
    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String sender;
    private String message;

    public ChatMessage toEntity(LocalDateTime ldt) {
        return ChatMessage.create(
                id,
                messageType.name(),
                chatRoomId,
                senderId.toString(),
                sender,
                message,
                ldt
        );
    }

    static public ChatMessageDto createFrom(ChatMessage entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.id = entity.getId();
        dto.messageType = MessageType.valueOf(entity.getMessageType()); 
        dto.chatRoomId = entity.getChatRoomId();
        dto.idx = entity.getIdx();
        dto.senderId = Long.parseLong(entity.getSenderId());
        dto.sender = entity.getSender();
        dto.message = entity.getMessage();
        return dto;
    }
}
