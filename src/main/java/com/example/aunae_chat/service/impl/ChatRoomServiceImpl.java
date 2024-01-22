package com.example.aunae_chat.service.impl;

import com.example.aunae_chat.data.documents.ChatMessage;
import com.example.aunae_chat.data.documents.ChatRoom;
import com.example.aunae_chat.data.domain.User;
import com.example.aunae_chat.repository.ChatMessageRepository;
import com.example.aunae_chat.repository.ChatRoomRepository;
import com.example.aunae_chat.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    // TODO: Change storage memory into MongoDB

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> all = chatRoomRepository.findAll();
        Collections.reverse(all);
        return all;
    }

    @Override
    public List<ChatMessage> findChatMessagesByRoomId(String roomId) {
//        ChatRoom room = findById(roomId);
        long roomIdL = Long.parseLong(roomId);
        List<ChatMessage> messages = chatMessageRepository.findChatMessagesByChatRoomId(roomIdL);
        return messages;
    }

    public ChatRoom findById(String roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(
                ()->new RuntimeException("ChatRoom not found by roomId: "+roomId)
        );
    }

    @Override
    public ChatRoom joinRoom(Long chatRoomId, Long userId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId).orElseThrow(() -> new RuntimeException("joinRoom error"));
        List<User> users = chatRoom.getUsers();
        User addUser = User.builder().userId(userId).username(username).build();
        users.add(addUser);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom joinRoomByBungaeId(Long bungaeId, Long userId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByBungaeId(bungaeId).orElseThrow(() -> new RuntimeException("joinRoom error"));
        List<User> users = chatRoom.getUsers();
        User addUser = User.builder().userId(userId).username(username).build();
        users.add(addUser);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom exitRoomByBungaeId(Long bungaeId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findByBungaeId(bungaeId).orElseThrow(()->new RuntimeException("exitRoom error"));

        List<User> users = chatRoom.getUsers();
        User selectedUser = users.stream().filter((user) ->
                user.getUserId().equals(userId)
        ).toList().get(0);
        users.remove(selectedUser);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom createRoom(String name, Long userId, String imageUrl, Long bungaeId) {
        ChatRoom chatRoom =
                ChatRoom.create(
                        name,
                        sequenceGeneratorService.generateSequence(ChatRoom.SEQUENCE_NAME),
                        imageUrl,
                        bungaeId
                );
        ChatRoom save = chatRoomRepository.save(chatRoom);
        log.info(save.toString());
        return save;
    }

    @Override
    public ChatRoom findChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId).orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
    }

    @Override
    public List<ChatRoom> findChatRoomByUser(Long userId) {
        return chatRoomRepository.findByUsers_UserId(userId);
    }

    @Override
    public boolean isChatRoomInUser(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(roomId).orElseThrow(() -> new RuntimeException("Error from findChatRoomInUser"));
        List<User> users = chatRoom.getUsers();
        User user = users.stream().filter(_user -> _user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        return user != null;
    }
}
