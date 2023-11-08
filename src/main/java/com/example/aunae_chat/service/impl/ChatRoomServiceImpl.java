package com.example.aunae_chat.service.impl;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.repository.ChatMessageRepository;
import com.example.aunae_chat.repository.ChatRoomRepository;
import com.example.aunae_chat.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    // TODO: Change storage memory into MongoDB

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> all = chatRoomRepository.findAll();
        Collections.reverse(all);
        return all;
    }

    @Override
    public List<ChatMessage> findChatMessagesByRoomId(String roomId) {
//        ChatRoom room = findById(roomId);
        long roomIdL = Long.parseLong(roomId);
        List<ChatMessage> messages = chatMessageRepository.findChatMessagesByChatRoomId(roomIdL);
        return messages;
    }

    public ChatRoom findById(String roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(
                ()->new RuntimeException("ChatRoom not found by roomId: "+roomId)
        );
    }

    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        ChatRoom save = chatRoomRepository.save(chatRoom);
        log.info(save.toString());
        return save;
    }
}
