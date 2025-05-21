package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.service.ChatMessageService;
import com.example.aunae_chat.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat")
@Slf4j
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
    public ResponseEntity<List<ChatRoom>> getChatRooms(HttpServletRequest request) {
        String username = (String)request.getAttribute("name");
        log.info("getChatRooms : {}", username);

        List<ChatRoom> allRoom = chatRoomService.findAllRoom();
        return ResponseEntity.ok().body(allRoom);
    }

    /**
     * 방 생성
     * @param name 방 이름
     * @param request 유저 리퀘스트
     * @return 방
     */
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(
            @RequestParam String name,
            HttpServletRequest request,
            @RequestParam(defaultValue = "null") String imageUrl,
            @RequestParam Long bungaeId) {
        Long userId = (Long)request.getAttribute("id");
        String username = (String) request.getAttribute("name");
        log.info("createRoom: {}", userId);
        ChatRoom room = chatRoomService.createRoom(name, userId, imageUrl, bungaeId);
        chatRoomService.joinRoom(room.getChatRoomId(), userId, username);

        return ResponseEntity.ok(room);
    }

    /**
     * 방 참가. 방을 참가해야 리스닝 가능.
     * @param roomId 방 아이디
     * @param request token
     * @return
     */
    @PostMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> joinRoom(@PathVariable Long roomId, HttpServletRequest request) {
        Long userId = (Long)request.getAttribute("id");
        String username = (String) request.getAttribute("name");
        ChatRoom chatRoom = chatRoomService.joinRoom(roomId, userId, username);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 방 퇴장.
     * @param bungaeId 번개 아이디
     * @param request token
     * @return
     */
    @DeleteMapping("/room/{bungaeId}")
    public ResponseEntity<ChatRoom> exitRoom(@PathVariable Long bungaeId, HttpServletRequest request, @RequestParam Long userId) {
        log.info("EXIT ROOM: user \"{}\" trying to kick user \"{}\"", request.getAttribute("id"), userId);
        ChatRoom chatRoom = chatRoomService.exitRoomByBungaeId(bungaeId, userId);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 본인이 참가한 채팅방들 얻기
     * @param request
     * @return 방 list
     */
    @GetMapping("/room/entered")
    public ResponseEntity<List<ChatRoom>> getRoomsJoined(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");
        List<ChatRoom> chatRoomByUser = chatRoomService.findChatRoomByUser(userId);
        return ResponseEntity.ok(chatRoomByUser);
    }

    /**
     * 본인이 미참여한 채팅방 리스트
     * @param request
     * @return
     */
    @GetMapping("/room/not-entered")
    public ResponseEntity<List<ChatRoom>> getRoomsNotJoined(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");
        List<ChatRoom> chatRoomByUser = chatRoomService.findChatRoomByUserNotPresent(userId);
        return ResponseEntity.ok(chatRoomByUser);
    }

    /**
     * 채팅방에 참여한 유저 리스트
     *
     */
    @GetMapping("/room/{roomId}/users")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable Long roomId, HttpServletRequest request) {
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomId(roomId);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 채팅방 정보 가져오기
     * @param roomId
     * @return
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> getLastMessages(@PathVariable Long roomId) {
        ChatRoom room = chatRoomService.findChatRoomByChatRoomId(roomId);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/room/{roomId}/leave")
    public ResponseEntity<String> leaveChatRoom(HttpServletRequest request, @PathVariable Long roomId) {
        Long userId = (Long) request.getAttribute("id");
        boolean b = chatRoomService.leaveRoom(roomId, userId);
        if(!b) return ResponseEntity.ok("요청 처리 실패");
        return ResponseEntity.ok("채팅방을 나갔습니다.");
    }

    /**
     * idx 이전의 메시지 가져오기
     * @param roomId
     * @param idx
     * @param num
     * @return
     */
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessagesById(@PathVariable Long roomId, @RequestParam(required = false) Long idx, @RequestParam(defaultValue = "20") int num) {
        List<ChatMessage> messages;
        if(idx == null) {
            messages = chatMessageService.findLastMessages(roomId, num);
        } else {
            messages = chatMessageService.findMessages(roomId, idx, num);
        }
        return ResponseEntity.of(Optional.ofNullable(messages));
    }
}
