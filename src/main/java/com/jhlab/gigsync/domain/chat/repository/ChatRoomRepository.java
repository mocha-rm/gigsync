package com.jhlab.gigsync.domain.chat.repository;

import com.jhlab.gigsync.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByUser1IdOrUser2Id(String user1Id, String user2Id);

    Optional<ChatRoom> findByUser1IdAndUser2Id(String user1Id, String user2Id);
}
