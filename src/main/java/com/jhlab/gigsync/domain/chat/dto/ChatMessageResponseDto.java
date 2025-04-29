package com.jhlab.gigsync.domain.chat.dto;

import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.type.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class ChatMessageResponseDto {
    private final String id;
    private final String roomId;
    private final String senderId;
    private final String senderNickName;
    private final String receiverId;
    private final String content;
    private final MessageType type;
    private final Instant timestamp;
    private final boolean isRead;

    public static ChatMessageResponseDto toDto(ChatMessage message) {
        return new ChatMessageResponseDto(
                message.getId(),
                message.getRoomId(),
                message.getSenderId(),
                message.getSenderNickName(),
                message.getReceiverId(),
                message.getContent(),
                message.getType(),
                message.getTimestamp(),
                message.isRead()
        );
    }
}
