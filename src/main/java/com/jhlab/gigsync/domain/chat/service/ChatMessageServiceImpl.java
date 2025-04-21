package com.jhlab.gigsync.domain.chat.service;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.repository.ChatMessageRepository;
import com.jhlab.gigsync.domain.chat.type.MessageType;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ChatMessageResponseDto saveMessage(Long receiverId, Long senderId, ChatMessageRequestDto requestDto) {
        User receiver = userService.getUserFromDB(receiverId);

        ChatMessage message = ChatMessage.builder()
                .receiverId(receiverId.toString())
                .senderId(senderId.toString())
                .content(requestDto.getContent())
                .roomId(senderId + "-" + receiverId)
                .messageType(MessageType.TEXT)
                .build();

        chatMessageRepository.save(message);

        return ChatMessageResponseDto.toDto(message);
    }

    @Override
    public List<ChatMessageResponseDto> getMessagesByRoom(String roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId);
        return chatMessages.stream()
                .map(ChatMessageResponseDto::toDto).toList();
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userId) {
        return chatMessageRepository.findByReceiverIdAndReadFalse(userId);
    }
}
