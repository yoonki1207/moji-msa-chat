package com.example.aunae_chat.data.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private MessageType messageType;
    private Long chatRoomId;
    private Long userId;
    private String username;
    private String content;
}
