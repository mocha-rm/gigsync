package com.jhlab.gigsync.global.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.repository.ChatMessageRepository;
import com.jhlab.gigsync.domain.chat.type.MessageType;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final JwtUtil jwtUtil;
    private final KafkaProducerService kafkaProducerService;
    private final WebSocketSessionManager sessionManager;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery(); // token=xxx&receiverId=2

        Map<String, String> params = Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2)) // "="으로 나누되 최대 2개로만 분리
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
        session.getAttributes().put("receiverId", receiverId); // 필요 시 저장
        sessionManager.register(userId, session);

        log.info("WebSocket 연결됨 - userId: {}, receiverId: {}", userId, receiverId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String senderId = (String) session.getAttributes().get("userId");
        String receiverId = (String) session.getAttributes().get("receiverId");

        // 텍스트만 받으므로 JSON 파싱 X
        String content = message.getPayload();

        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(MessageType.TEXT)
                .roomId(Stream.of(senderId, receiverId).sorted().collect(Collectors.joining("-")))
                .build();

        chatMessageRepository.save(chatMessage);

        String messageJson = objectMapper.writeValueAsString(chatMessage);
        kafkaProducerService.sendMessage(messageJson);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.unregister(session);
    }
}
