package com.jhlab.gigsync.domain.chat.repository;

import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

    List<ChatMessage> findByRoomIdAndReceiverIdAndIsReadFalse(String roomId, String userId);
}
