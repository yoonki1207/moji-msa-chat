package com.example.aunae_chat.service;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoom> findAllRoom();
    List<ChatMessage> findChatMessagesByRoomId(String roomId);
    ChatRoom findById(String roomId);
    ChatRoom joinRoom(Long chatRoomId, Long userId, String username);
    ChatRoom createRoom(String name, Long userId, String imageUrl);
    ChatRoom findChatRoomByChatRoomId(Long chatRoomId);
    List<ChatRoom> findChatRoomByUser(Long userId);
}
