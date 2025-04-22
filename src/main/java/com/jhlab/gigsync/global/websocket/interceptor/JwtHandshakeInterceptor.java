package com.jhlab.gigsync.global.websocket.interceptor;

import com.jhlab.gigsync.global.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            String token = httpServletRequest.getParameter("token");

            if (token == null || !jwtUtil.validateToken(token)) {
                return false; // 토큰 없거나 유효하지 않으면 연결 거절
            }

            String isLogout = (String) redisTemplate.opsForValue().get(token);
            if ("logout".equals(isLogout)) {
                return false;
            }

            String receiverId = servletRequest.getServletRequest().getParameter("receiverId");
            if (!StringUtils.hasText(receiverId)) {
                return false;
            }

            String userId = String.valueOf(jwtUtil.getUserId(token)); // 유저 ID 추출
            attributes.put("userId", userId); // 이후 handleTextMessage 에서 사용 가능
            attributes.put("receiverId", receiverId);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
