package com.jhlab.gigsync.domain.chat.controller;

import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.service.ChatMessageService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
import jakarta.validation.Valid;
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

    @PostMapping("/{receiverId}")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(@PathVariable Long receiverId,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestBody @Valid ChatMessageRequestDto requestDto) {
        return new ResponseEntity<>(chatMessageService.saveMessage(receiverId, userDetails.getUser().getId(), requestDto), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public List<ChatMessage> getRoomMessages(@PathVariable String roomId) {
        return chatMessageService.getMessagesByRoom(roomId);
    }
}
