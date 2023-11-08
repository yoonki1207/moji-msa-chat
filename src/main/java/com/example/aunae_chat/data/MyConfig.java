package com.example.aunae_chat.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@Setter
@Getter
@ConfigurationProperties(prefix = "tang")
@RefreshScope
@ToString
public class MyConfig {
    private String profile;
    private String comment;
}
