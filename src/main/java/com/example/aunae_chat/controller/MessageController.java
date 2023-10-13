package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

/**
 * prefix `/app`
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto messageDto) {
        log.info("Message: {}", messageDto.toString());
        if (ChatMessageDto.MessageType.ENTER.equals(messageDto.getMessageType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/room/" + messageDto.getChatRoomId(), messageDto);
    }
}
