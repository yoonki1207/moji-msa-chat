package com.example.aunae_chat.service;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.dto.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
    ChatMessageDto saveMessage(ChatMessageDto dto);
    List<ChatMessage> findMessages(Long ChatRoomId, Long idx, int num);
    List<ChatMessage> findLastMessages(Long chatRoomId, int num);
}
