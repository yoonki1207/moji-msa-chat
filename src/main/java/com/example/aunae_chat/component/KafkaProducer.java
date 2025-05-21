package com.example.aunae_chat.component;

import com.example.aunae_chat.data.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private KafkaTemplate<String, ChatMessageDto> kafkaTemplate;

    public void send(String topic, ChatMessageDto payload) {
        log.info("sending payload={} to topic ={}", payload, topic);
        kafkaTemplate.send(topic, payload);
    }
}
