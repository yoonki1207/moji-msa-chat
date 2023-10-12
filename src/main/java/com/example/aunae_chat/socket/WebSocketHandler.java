package com.example.aunae_chat.socket;

import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    // connected sessions
    private static final Set<WebSocketSession> sessions = new HashSet<>();

    // chat room
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Autowired
    public WebSocketHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        ChatMessageDto chatMessageDto = mapper.readValue(
                payload, ChatMessageDto.class
        );
        log.info("session {}", chatMessageDto.getChatRoomId());

        Long chatRoomId = chatMessageDto.getChatRoomId();

        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageDto
                .getMessageType()
                .equals(ChatMessageDto.MessageType.ENTER)) {
            chatRoomSession.add(session);
        }

        // remove closed session
        if(chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }

        sendMessageToChatRoom(chatMessageDto, chatRoomSession);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(
                sess -> sendMessage(sess, chatMessageDto)
        );
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(
                    new TextMessage(mapper.writeValueAsString(message))
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
