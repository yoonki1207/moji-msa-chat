package com.example.aunae_chat;

import com.example.aunae_chat.data.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MyConfig.class)
public class AunaeChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AunaeChatApplication.class, args);
    }

}
