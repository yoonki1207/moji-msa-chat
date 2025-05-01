package com.example.aunae_chat.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class UserDto {
    private String name;
    private String email;
    private String roles;
    private Long id;

    public Map<String, Object> getPayload() {
        Map<String , Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("roles", roles);
        payload.put("id", id);
        return payload;
    }
}
