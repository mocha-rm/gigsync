package com.jhlab.gigsync.domain.chat.dto;

import com.jhlab.gigsync.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private String roomId;
    private Long otherUserId;
    private String otherUserNickName;
    private String lastMessage;
    private String lastMessageTime;
    private int unreadCount;

    public static ChatRoomResponseDto from(ChatRoom room, Long currentUserId, String otherUserNickName) {
        String otherUserId = room.getUser1Id().equals(currentUserId.toString())
                ? room.getUser2Id()
                : room.getUser1Id();

        return ChatRoomResponseDto.builder()
                .roomId(room.getId())
                .otherUserId(Long.parseLong(otherUserId))
                .otherUserNickName(otherUserNickName)
                .lastMessage(room.getLastMessage())
                .lastMessageTime(room.getLastMessageTime().toString())
                .unreadCount(room.getUnreadCount())
                .build();
    }
}
