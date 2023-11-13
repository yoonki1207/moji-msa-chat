package com.example.aunae_chat.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WebSocketHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    private final AuthorizationExtractor authExtractor;

    public WebSocketHandShakeInterceptor(AuthorizationExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();

            String token = authExtractor.extract(servletRequest, "Bearer");
            if (token == null || "".equals(token)) {
                return true;
            }
            String[] chuncks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            // decode payload
            String decode = new String(decoder.decode(chuncks[1]));
            JsonParser jsonParser = new BasicJsonParser();

            Map<String, Object> jsonArray = jsonParser.parseMap(decode);

            // assert user is valid

            // TODO: attributes 에 name 혹은 nickname 있어야함.
            attributes.put("name", jsonArray.get("email"));
            attributes.put("id", jsonArray.get("id"));
            attributes.put("email", jsonArray.get("email"));
            attributes.put("roles", jsonArray.get("roles"));
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
