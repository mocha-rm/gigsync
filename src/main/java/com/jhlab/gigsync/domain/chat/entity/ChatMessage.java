package com.jhlab.gigsync.domain.chat.entity;

import com.jhlab.gigsync.domain.chat.type.MessageType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    private String id;

    private String senderId;
    private String senderNickName;
    private String receiverId;
    private String roomId;

    private MessageType type;
    private String content;

    private Instant timestamp;

    @Setter
    private boolean isRead;

    @Builder
    public ChatMessage(String senderId, String senderNickName, String receiverId, String roomId, MessageType messageType, String content) {
        this.senderId = senderId;
        this.senderNickName = senderNickName;
        this.receiverId = receiverId;
        this.roomId = roomId;
        this.type = messageType;
        this.content = content;
        this.timestamp = Instant.now();
        this.isRead = false;
    }
}
