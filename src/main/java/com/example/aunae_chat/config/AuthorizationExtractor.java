package com.example.aunae_chat.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.Objects;

@Component
public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            String extracted = extract(value, type);
            if(!Objects.equals(extracted, Strings.EMPTY)) {
                return extracted;
            }
        }
        return Strings.EMPTY;
    }

    public String extract(String headerValue, String type) {
        if(headerValue.toLowerCase().startsWith(type.toLowerCase())) {
            return headerValue.substring(type.length()).trim();
        }
        return Strings.EMPTY;
    }
}
