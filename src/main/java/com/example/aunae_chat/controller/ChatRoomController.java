package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.example.aunae_chat.service.ChatMessageService;
import com.example.aunae_chat.service.ChatRoomService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    // TODO: 채팅방 입장 시 지금까지 썼던 모든 채팅 기록을 최신 순으로 순차적으로 접근할 수 있는 기능

    final private ChatRoomService chatRoomService;
    final private ChatMessageService chatMessageService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, ChatMessageService chatMessageService) {
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("Invalid url.");
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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessage>> getLastMessages(@PathVariable Long roomId, @RequestParam(defaultValue = "20") int num) {
        List<ChatMessage> result = chatMessageService.findLastMessages(roomId, num);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessagesById(@PathVariable Long roomId, @RequestParam Long idx, @RequestParam(defaultValue = "3") int num) {
        List<ChatMessage> messages = chatMessageService.findMessages(roomId, idx, num);
        return ResponseEntity.of(Optional.ofNullable(messages));
    }
}
