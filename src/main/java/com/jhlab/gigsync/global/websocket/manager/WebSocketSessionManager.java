package com.jhlab.gigsync.global.websocket.manager;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void unregister(WebSocketSession session) {
        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }

    public WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }
}
