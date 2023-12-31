package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.example.aunae_chat.service.ChatMessageService;
import com.example.aunae_chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.Assert;
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
    private final ChatRoomService chatRoomService;

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

        Long roomId = messageDto.getChatRoomId();
        ChatRoom room = chatRoomService.findChatRoomByChatRoomId(roomId);
        Assert.notNull(room, "Room은 null일 수 없습니다.");
        log.info("Room and message: {}, {}", room.getChatRoomId(), messageDto.getMessage());
        switch(messageDto.getMessageType()) {
            case ENTER -> {
                sendEnterMessage(messageDto);
            }
            case TALK -> {
                sendMessage(messageDto);
            }
            case LEAVE -> {
            }
            case MEDIA -> {
            }
            case NOTICE -> {
                sendNoticeMessage(messageDto);
            }
        }
    }

    public void sendEnterMessage(ChatMessageDto messageDto) {
        boolean chatRoomInUser = chatRoomService.isChatRoomInUser(messageDto.getSenderId(), messageDto.getChatRoomId());
        if(!chatRoomInUser) {
            messageDto.setMessage(messageDto.getSender() + "님이 입장하였습니다.");
        }
    }

    public void sendNoticeMessage(ChatMessageDto messageDto) {
        // TODO: '@@'로 분리하여 좌측은 Title, 우측은 Content 로 저장.
        String message = messageDto.getMessage();
        String[] split = message.split("@@"); // split말고 첫 @@만 분리
    }

    public void sendMessage(ChatMessageDto messageDto) {
        Long roomId = messageDto.getChatRoomId();

        // 메시지 DB에 저장
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(messageDto);

        // TODO: 멘션이 포함되어있는지 확인 후 푸쉬 알람 요청

        // 메시지 뿌리기
        sendingOperations.convertAndSend("/topic/chat/room/" + roomId, savedMessageDto);

    }
}
