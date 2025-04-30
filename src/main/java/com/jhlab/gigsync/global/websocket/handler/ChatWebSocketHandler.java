package com.jhlab.gigsync.global.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageRequestDto;
import com.jhlab.gigsync.domain.chat.dto.ChatMessageResponseDto;
import com.jhlab.gigsync.domain.chat.service.ChatMessageService;
import com.jhlab.gigsync.global.kafka.KafkaProducerService;
import com.jhlab.gigsync.global.security.utils.JwtUtil;
import com.jhlab.gigsync.global.websocket.manager.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final JwtUtil jwtUtil;
    private final KafkaProducerService kafkaProducerService;
    private final WebSocketSessionManager sessionManager;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery(); // token=xxx&receiverId=2

        Map<String, String> params = Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));

        String token = params.get("token");
        String receiverId = params.get("receiverId");

        if (token == null || receiverId == null) {
            log.error("WebSocket 연결 실패: 쿼리 파라미터 누락");
            try {
                session.close();
            } catch (IOException e) {
                log.error("세션 종료 실패", e);
            }

            return;
        }

        String userId = String.valueOf(jwtUtil.getUserId(token));
        session.getAttributes().put("userId", userId);
        session.getAttributes().put("receiverId", receiverId);
        sessionManager.register(userId, session);

        log.info("WebSocket 연결됨 - userId: {}, receiverId: {}", userId, receiverId);

        try {
            String roomId = generateRoomId(Long.parseLong(userId), Long.parseLong(receiverId));
            log.info("채팅방 Id: {}", roomId);
            session.getAttributes().put("roomId", roomId);

            List<ChatMessageResponseDto> messages = chatMessageService.getMessagesByRoom(roomId, Long.parseLong(userId));

            for (ChatMessageResponseDto message : messages) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }

            chatMessageService.markMessagesAsRead(roomId, Long.parseLong(receiverId));
        } catch (Exception e) {
            log.error("과거 메시지 전송 실패", e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String senderId = (String) session.getAttributes().get("userId");
        String receiverId = (String) session.getAttributes().get("receiverId");

        String content = message.getPayload();

        ChatMessageRequestDto requestDto = new ChatMessageRequestDto(content);
        ChatMessageResponseDto savedMessage = chatMessageService.saveMessage(
                Long.parseLong(receiverId),
                Long.parseLong(senderId),
                requestDto
        );

        String messageJson = objectMapper.writeValueAsString(savedMessage);
        kafkaProducerService.sendMessage(messageJson);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.unregister(session);
    }

    private String generateRoomId(Long user1Id, Long user2Id) {
        return user1Id < user2Id
                ? user1Id + "-" + user2Id
                : user2Id + "-" + user1Id;
    }
}
