package com.jhlab.gigsync.global.common.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;

    private static final String EMAIL_VERIFICATION_PREFIX = "email:verification:";

    public void sendVerificationCode(String email) throws MessagingException {
        String code = emailService.sendSimpleMessage(email);
        String redisKey = EMAIL_VERIFICATION_PREFIX + email;

        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(3));
    }

    public boolean verifyCode(String email, String code) {
        String redisKey = EMAIL_VERIFICATION_PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        return savedCode != null && savedCode.equals(code);
    }

    public void removeCode(String email) {
        String redisKey = EMAIL_VERIFICATION_PREFIX + email;
        redisTemplate.delete(redisKey);
    }
}
