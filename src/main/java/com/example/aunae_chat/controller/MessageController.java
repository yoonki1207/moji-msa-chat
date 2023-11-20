package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.example.aunae_chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    private final ChatMessageService chatMessageService;

    /**
     * Receive message
     * @param messageDto Message DTO
     */
    @MessageMapping("/chat/message")
    public void enter(SimpMessageHeaderAccessor accessor, ChatMessageDto messageDto) {
//        Long senderId = (Long) accessor.getSessionAttributes().get("id");
//        String senderName = (String) accessor.getSessionAttributes().get("name");
//
//
//        // set sender data with session
//        messageDto.setSenderId(senderId);
//        messageDto.setSender(senderName);

        // TODO: 방 확인 후 존재하면 입장 메시지 전송, else 무시
        if (ChatMessageDto.MessageType.ENTER.equals(messageDto.getMessageType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 입장하였습니다.");
        }
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(messageDto);
        sendingOperations.convertAndSend("/topic/chat/room/" + messageDto.getChatRoomId(), savedMessageDto);
    }
}
