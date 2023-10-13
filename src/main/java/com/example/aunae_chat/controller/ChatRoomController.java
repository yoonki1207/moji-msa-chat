package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    final private ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/room")
    public ResponseEntity<List<ChatRoom>> getChatRooms() {
        List<ChatRoom> allRoom = chatRoomService.findAllRoom();
        return ResponseEntity.ok().body(allRoom);
    }

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomService.createRoom(name);
    }
}
