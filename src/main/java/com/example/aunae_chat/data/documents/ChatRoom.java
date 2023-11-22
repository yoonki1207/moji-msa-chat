package com.example.aunae_chat.data.documents;

import com.example.aunae_chat.data.domain.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "chatRoom")
@ToString
public class ChatRoom {

    @Transient
    public static String SEQUENCE_NAME = "chatroom_sequence";

    @Id
    private String id;
    private Long chatRoomId;
    private Long bungaeId;
    private String roomName;
    private String imageUrl;
    private List<User> users;

    public static ChatRoom create(String name, Long chatRoomId, String imageUrl, Long bungaeId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(chatRoomId);
        chatRoom.setRoomName(name);
        chatRoom.setBungaeId(bungaeId);
        chatRoom.setImageUrl(imageUrl);
        chatRoom.setUsers(new ArrayList<>());
        return chatRoom;
    }
}
