package com.example.aunae_chat.controller;

import com.example.aunae_chat.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ws-auth")
public class AuthController {

    final private ChatRoomService chatRoomService;

    @PostMapping("/chat/{roomId}")
    public ResponseEntity<?> authenticateWebSocket(HttpServletRequest request, @PathVariable String roomId) {
        Object id = request.getSession().getAttribute("id");
        if(id != null) log.info("!!: {}", id);
        Long userId = (Long)request.getAttribute("id");
        if(userId != null) {
            try {
                boolean chatRoomInUser = chatRoomService.isChatRoomInUser(userId, Long.parseLong(roomId));
                if(chatRoomInUser) {
                    request.getSession().setAttribute("id", userId);
                    return ResponseEntity.ok().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
