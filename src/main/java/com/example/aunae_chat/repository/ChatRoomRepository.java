package com.example.aunae_chat.repository;

import com.example.aunae_chat.data.documents.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByRoomName(String roomName);
    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);
    List<ChatRoom> findByUsers_UserId(Long userId);
}
