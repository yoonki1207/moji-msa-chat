package com.example.aunae_chat.service.impl;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.dto.ChatMessageDto;
import com.example.aunae_chat.repository.ChatMessageRepository;
import com.example.aunae_chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    final private ChatMessageRepository chatMessageRepository;
    final private SequenceGeneratorService sequenceGeneratorService;
    @Override
    public ChatMessageDto saveMessage(ChatMessageDto dto) {
        LocalDateTime now = LocalDateTime.now();
        ChatMessage entity = dto.toEntity(now);

        entity.setIdx(sequenceGeneratorService.generateSequence(ChatMessage.SEQUENCE_NAME));

        ChatMessage save = chatMessageRepository.save(entity);

        return ChatMessageDto.createFrom(save);
    }

    @Override
    public List<ChatMessage> findMessages(Long chatRoomId, Long idx, int num) {
        // TODO: id를 제외한 이전 채팅 num 개 반환
        // db.chatMessages.find({idx: {$lt: 6}}).sort({idx:-1}).limit(3).sort({idx:1});
        List<ChatMessage> messages = chatMessageRepository
                .findByChatRoomIdAndIdxLessThanOrderByIdxAsc(
                        chatRoomId,
                        idx,
                        PageRequest.of(0, num)
                );
        return messages;
    }
}
