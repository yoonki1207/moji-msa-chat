package com.example.aunae_chat.data.documents;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chatRoom")
@ToString
public class ChatRoom {
    @Id
    private String id;
    private String roomName;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(name);
        return chatRoom;
    }
}
