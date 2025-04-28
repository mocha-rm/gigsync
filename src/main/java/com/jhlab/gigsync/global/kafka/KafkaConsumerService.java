package com.jhlab.gigsync.global.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhlab.gigsync.domain.chat.entity.ChatMessage;
import com.jhlab.gigsync.global.websocket.manager.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(String messageJson) {
        try {
            ChatMessage message = objectMapper.readValue(messageJson, ChatMessage.class);

            WebSocketSession receiverSession = sessionManager.getSession(message.getReceiverId());
            log.info("Receiver ID: {}", message.getReceiverId());
            log.info("Receiver session: {}", receiverSession);

            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(messageJson));
            } else {
                log.info("수신자가 오프라인입니다. 메시지를 저장합니다.");
            }

        } catch (Exception e) {
            log.error("Kafka consumer error", e);
        }
    }
}
