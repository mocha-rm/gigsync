package com.jhlab.gigsync.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "chatRooms")
@Getter
@Setter
@Builder
public class ChatRoom {
    @Id
    private String id;
    private String user1Id;
    private String user2Id;
    private String lastMessage;
    private Instant lastMessageTime;
    private int unreadCount;
}
