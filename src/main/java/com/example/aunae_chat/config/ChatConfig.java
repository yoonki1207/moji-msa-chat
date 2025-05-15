package com.example.aunae_chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ChatConfig.class);
    final private WebSocketHandShakeInterceptor webSocketHandShakeInterceptor;
    final private AuthorizationExtractor authorizationExtractor;

    public ChatConfig(WebSocketHandShakeInterceptor webSocketHandShakeInterceptor, AuthorizationExtractor authorizationExtractor) {
        this.webSocketHandShakeInterceptor = webSocketHandShakeInterceptor;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .addInterceptors(webSocketHandShakeInterceptor)
//                .setAllowedOrigins("http://localhost:3000")
                .setAllowedOriginPatterns("*").withSockJS().setSuppressCors(true);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        log.info("Registering client inbound channel");
        registration.interceptors(new BearerAuthInterceptor(authorizationExtractor));
    }
}
