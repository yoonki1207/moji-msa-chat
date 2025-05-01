package com.example.aunae_chat.controller;

import com.example.aunae_chat.data.MyConfig;
import com.example.aunae_chat.data.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/config")
public class ConfigController {

    private final MyConfig myConfig;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @GetMapping
    public ResponseEntity<String> config() {
        System.out.println(myConfig);
        return ResponseEntity.ok(myConfig.toString());
    }

    @PostMapping("/generate-jwt")
    public ResponseEntity<Map<String, String>> generateJwt(@RequestBody UserDto userDto) {
        String token = Jwts.builder()
                .setClaims(userDto.getPayload())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hours
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
        return ResponseEntity.ok(Map.of("token", token));
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }
}
