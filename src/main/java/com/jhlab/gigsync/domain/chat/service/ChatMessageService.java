package com.jhlab.gigsync.domain.chat.service;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponseDto saveMessage(Long receiverId, Long senderId, ChatMessageRequestDto requestDto);

    List<ChatMessageResponseDto> getMessagesByRoom(String roomId, Long userId);

    List<ChatRoomResponseDto> getMyRooms(Long userId);

    void markMessagesAsRead(String roomId, Long userId);
}
