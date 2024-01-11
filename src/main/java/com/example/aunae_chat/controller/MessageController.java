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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        final String separator = "@@";
//        String message = messageDto.getMessage();
//        int index = message.indexOf(separator);
//        if(index < 0) {
//            return;
//        }
//        String title = message.substring(0, index);
//        String content = message.substring(index + separator.length());

        sendMessage(messageDto);
    }

    public List<String> getMentionedUserId(String message) {
        List<String> allMatchers = new ArrayList<>();
        Matcher m = Pattern.compile("@\\{([0-9]+)};")
                .matcher(message);
        while(m.find()) {
            allMatchers.add(m.group(1));
        }
        return allMatchers;
    }

    public void sendMessage(ChatMessageDto messageDto) {
        Long roomId = messageDto.getChatRoomId();

        // 메시지 DB에 저장
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(messageDto);

        List<String> userIdList = getMentionedUserId(savedMessageDto.getMessage());
        if(userIdList.size() > 0) {
            // TODO: push 알림 보내기

        }

        // 메시지 뿌리기
        sendingOperations.convertAndSend("/topic/chat/room/" + roomId, savedMessageDto);
    }
}
