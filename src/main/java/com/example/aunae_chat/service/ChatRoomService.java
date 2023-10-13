package com.example.aunae_chat.service;

import com.example.aunae_chat.data.documents.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoom> findAllRoom();
    ChatRoom findById(String roomId);
    ChatRoom createRoom(String name);
}
