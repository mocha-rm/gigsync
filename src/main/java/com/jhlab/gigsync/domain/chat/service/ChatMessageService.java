package com.jhlab.gigsync.domain.chat.service;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponseDto saveMessage(Long receiverId, Long senderId, ChatMessageRequestDto requestDto);

    List<ChatMessageResponseDto> getMessagesByRoom(String roomId);

    List<ChatMessage> getUnreadMessages(String userId);
}
