package com.jhlab.gigsync.global.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhlab.gigsync.domain.chat.dto.ChatMessagePayloadDto;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.domain.chat.repository.ChatMessageRepository;
import com.jhlab.gigsync.domain.chat.type.MessageType;
import com.jhlab.gigsync.global.kafka.KafkaProducerService;
import com.jhlab.gigsync.global.websocket.manager.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final KafkaProducerService kafkaProducerService;
    private final WebSocketSessionManager sessionManager;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        String userId = query.split("=")[1];
        session.getAttributes().put("userId", userId);
        sessionManager.register(userId, session);
        log.info("WebSocket 연결됨 - userId: {}", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String senderId = (String) session.getAttributes().get("userId");

        ChatMessagePayloadDto payloadDto = objectMapper.readValue(message.getPayload(), ChatMessagePayloadDto.class);

        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(senderId)
                .receiverId(payloadDto.getReceiverId())
                .content(payloadDto.getContent())
                .messageType(MessageType.TEXT)
                .roomId(senderId + "-" + payloadDto.getReceiverId())
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
