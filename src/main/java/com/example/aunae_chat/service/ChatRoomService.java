package com.example.aunae_chat.service;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoom> findAllRoom();
    List<ChatMessage> findChatMessagesByRoomId(String roomId);
    ChatRoom findById(String roomId);
    ChatRoom joinRoom(Long chatRoomId, Long userId, String username);
    ChatRoom joinRoomByBungaeId(Long bungaeId, Long userId, String username);
    ChatRoom exitRoomByBungaeId(Long bungaeId, Long userId);
    ChatRoom createRoom(String name, Long userId, String imageUrl, Long bungaeId);
    ChatRoom findChatRoomByChatRoomId(Long chatRoomId);
    List<ChatRoom> findChatRoomByUser(Long userId);
    List<ChatRoom> findChatRoomByUserNotPresent(Long userId);
    boolean isChatRoomInUser(Long userId, Long roomId);
}
