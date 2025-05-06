package com.example.aunae_chat.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
public class BearerAuthInterceptor implements HandlerInterceptor, ChannelInterceptor {
    private final AuthorizationExtractor authExtractor;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        log.info("full message: {}", message);
        log.info("auth: {}", headerAccessor.getNativeHeader("Authorization"));

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.info("msg: Connected");
            // TODO: user validation logic. check the room has user
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>> interceptor.preHandle 호출");
        String token = authExtractor.extract(request, "Bearer");
        if (token == null || "".equals(token)) {
            return true;
        }
        Map<String, Object> jsonArray = getPayload(token);
        request.setAttribute("name", jsonArray.get("name"));
        request.setAttribute("id", jsonArray.get("id"));
        request.setAttribute("email", jsonArray.get("email"));
        request.setAttribute("roles", jsonArray.get("roles"));
        return true;
    }

    public Map<String, Object> getPayload(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        // decode payload
        String decode = new String(decoder.decode(chunks[1]));
        JsonParser jsonParser = new BasicJsonParser();
        return jsonParser.parseMap(decode);
    }
}
