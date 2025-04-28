package com.jhlab.gigsync.domain.chat.controller;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.dto.ChatRoomResponseDto;
import com.jhlab.gigsync.domain.chat.service.ChatMessageService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @Operation(summary = "메시지 가져오기")
    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageResponseDto> getRoomMessages(@PathVariable String roomId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatMessageService.getMessagesByRoom(roomId, userDetails.getUser().getId());
    }

    @Operation(summary = "채팅방 목록 조회")
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getMyRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(chatMessageService.getMyRooms(userDetails.getUser().getId()), HttpStatus.OK);
    }

    @Operation(summary = "메시지 읽음 처리")
    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markMessagesAsRead(@PathVariable String roomId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatMessageService.markMessagesAsRead(roomId, userDetails.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
