package com.example.aunae_chat.repository;

import com.example.aunae_chat.data.documents.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findChatMessagesByChatRoomId(Long roomId);
    List<ChatMessage> findByChatRoomIdAndIdxLessThanOrderByIdxDesc(Long ChatRoomId, Long idx, Pageable pageable);
    List<ChatMessage> findByChatRoomIdOrderByIdxDesc(Long ChatRoomId, Pageable pageable);
}
