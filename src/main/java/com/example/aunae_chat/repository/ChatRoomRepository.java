package com.example.aunae_chat.repository;

import com.example.aunae_chat.data.documents.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByRoomName(String roomName);
    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);
    Optional<ChatRoom> findByBungaeId(Long bungaeId);
    List<ChatRoom> findByUsers_UserId(Long userId);

    // user가 미참여한 채팅방 리스트
    @Query("{ 'users':  { $not : { $elemMatch:  {'userId': ?0}}}}")
    List<ChatRoom> findAllByUserIdNotPresent(Long userId);
}
