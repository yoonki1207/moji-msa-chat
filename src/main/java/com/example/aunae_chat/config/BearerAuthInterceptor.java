package com.example.aunae_chat.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>> interceptor.preHandle 호출");
        String token = authExtractor.extract(request, "Bearer");
        if (token == null || "".equals(token)) {
            return true;
        }
        log.info(">>> token: {}", token);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        // decode payload
        String decode = new String(decoder.decode(chunks[1]));
        JsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonArray = jsonParser.parseMap(decode);
        log.info(">>> 유저 정보: {}", jsonArray);
        request.setAttribute("id", jsonArray.get("id"));
        request.setAttribute("email", jsonArray.get("email"));
        request.setAttribute("roles", jsonArray.get("roles"));
        return true;
    }
}
