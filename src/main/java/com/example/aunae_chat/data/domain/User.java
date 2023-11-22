package com.example.aunae_chat.data.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long userId;
    private String username;
}
