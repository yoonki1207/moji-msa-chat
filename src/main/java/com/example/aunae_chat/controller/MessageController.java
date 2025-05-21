package com.example.aunae_chat.controller;

import com.example.aunae_chat.config.AuthorizationExtractor;
import com.example.aunae_chat.config.BearerAuthInterceptor;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.example.aunae_chat.data.dto.NoticeMessageDto;
import com.example.aunae_chat.service.ChatMessageService;
import com.example.aunae_chat.service.ChatRoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final ObjectMapper mapper;

    /**
     * Receive message
     * @param messageDto Message DTO
     */
    @MessageMapping("/chat/message")
    public void enter(SimpMessageHeaderAccessor accessor, ChatMessageDto messageDto) throws JsonProcessingException {

        Long roomId = messageDto.getChatRoomId();
        ChatRoom room = chatRoomService.findChatRoomByChatRoomId(roomId);
//        room.getUsers().contains()
        Assert.notNull(room, "Room은 null일 수 없습니다.");
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
            sendMessage(messageDto);
        }
    }

    public void sendNoticeMessage(ChatMessageDto messageDto) throws JsonProcessingException {
        String json = messageDto.getMessage();
        NoticeMessageDto noticeMessageDto = mapper.readValue(json, NoticeMessageDto.class);
        // push 알림
        sendMessage(messageDto);
    }

    public List<String> getMentionedUserId(String message) {
        List<String> allMatchers = new ArrayList<>();
        Matcher m = Pattern.compile("@\\[([\\w|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+)]\\(([0-9]+)\\)")
                .matcher(message);
        while(m.find()) {
            allMatchers.add(m.group(2)); // 이름, group(2)는 id
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
