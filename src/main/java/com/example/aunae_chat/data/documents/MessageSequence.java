package com.example.aunae_chat.data.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "message_sequence")
public class MessageSequence {

    @Id
    private String id;
    private long seq;
}
