package com.jhlab.gigsync.domain.chat.service;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.dto.ChatRoomResponseDto;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.entity.ChatRoom;
import com.jhlab.gigsync.domain.chat.repository.ChatMessageRepository;
import com.jhlab.gigsync.domain.chat.repository.ChatRoomRepository;
import com.jhlab.gigsync.domain.chat.type.MessageType;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ChatMessageResponseDto saveMessage(Long receiverId, Long senderId, ChatMessageRequestDto requestDto) {
        User receiver = userService.getUserFromDB(receiverId);
        String roomId = generateRoomId(senderId, receiverId);

        ChatRoom room = findOrCreateRoom(senderId, receiverId);
        room.setId(roomId);

        ChatMessage message = ChatMessage.builder()
                .receiverId(receiverId.toString())
                .senderId(senderId.toString())
                .content(requestDto.getContent())
                .roomId(roomId)
                .messageType(MessageType.TEXT)
                .build();

        chatMessageRepository.save(message);

        room.setLastMessage(requestDto.getContent());
        room.setLastMessageTime(Instant.now());
        if (!senderId.toString().equals(receiverId.toString())) {
            room.setUnreadCount(room.getUnreadCount() + 1);
        }

        chatRoomRepository.save(room);

        return ChatMessageResponseDto.toDto(message);
    }

    @Override
    public List<ChatMessageResponseDto> getMessagesByRoom(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId);
        return chatMessages.stream()
                .map(ChatMessageResponseDto::toDto).toList();
    }

    @Override
    public List<ChatRoomResponseDto> getMyRooms(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByUser1IdOrUser2Id(
                userId.toString(),
                userId.toString()
        );

        return rooms.stream()
                .map(room -> {
                    String otherUserId = room.getUser1Id().equals(userId.toString())
                            ? room.getUser2Id()
                            : room.getUser1Id();
                    User otherUser = userService.getUserFromDB(Long.parseLong(otherUserId));

                    return ChatRoomResponseDto.from(room, userId, otherUser.getNickName());
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markMessagesAsRead(String roomId, Long userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByRoomIdAndReceiverIdAndIsReadFalse(roomId, String.valueOf(userId));
        unreadMessages.forEach(message -> message.setRead(true));
        chatMessageRepository.saveAll(unreadMessages);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        chatRoom.setUnreadCount(0);
        chatRoomRepository.save(chatRoom);
    }

    private ChatRoom findOrCreateRoom(Long senderId, Long receiverId) {
        return chatRoomRepository.findByUser1IdAndUser2Id(senderId.toString(), receiverId.toString())
                .or(() -> chatRoomRepository.findByUser1IdAndUser2Id(receiverId.toString(), senderId.toString()))
                .orElseGet(() -> ChatRoom.builder()
                        .user1Id(senderId.toString())
                        .user2Id(receiverId.toString())
                        .unreadCount(0)
                        .build()
                );
    }

    private String generateRoomId(Long user1Id, Long user2Id) {
        return user1Id < user2Id
                ? user1Id + "-" + user2Id
                : user2Id + "-" + user1Id;
    }
}
