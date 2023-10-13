package com.example.aunae_chat.service.impl;

import com.example.aunae_chat.data.documents.ChatRoom;
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

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> all = chatRoomRepository.findAll();
        Collections.reverse(all);
        return all;
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
