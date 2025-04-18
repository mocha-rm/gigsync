package com.jhlab.gigsync.global.security.utils;

import com.jhlab.gigsync.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Getter
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    /**
     * Access Token 생성
     * @param user {@link User}
     * @return Access Token
     */
    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpirationTime);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpirationTime);
    }

    /**
     * JWT 생성
     * @param user {@link User}
     * @param expireTime 만료 시간
     * @return JWT
     */
    private String generateToken(User user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpiredTime = now.plus(Duration.ofMillis(expireTime));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenExpiredTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 User Id 추출
     * @param token 토큰
     * @return User Id
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    /**
     * 토큰에서 User Email 추출
     * @param token 토큰
     * @return User Email
     */
    public String getUserEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    /**
     * Token 만료 시간 가져오기
     * @param token 엑세스 토큰
     * @return 토큰 만료 시간
     */
    public long getTokenExpirationTime(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    /**
     * JWT 검증
     * @param token 토큰
     * @return 유효하면 True
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException exception) {
            log.info("Invalid JWT Token", exception);
        } catch (ExpiredJwtException exception) {
            log.info("Expired JWT Token", exception);
        } catch (UnsupportedJwtException exception) {
            log.info("Unsupported JWT Token", exception);
        } catch (IllegalArgumentException exception) {
            log.info("JWT claims string is empty", exception);
        }

        return false;
    }

    /**
     * JWT Claims 추출
     * @param accessToken 엑세스 토큰
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException exception) {
            return exception.getClaims();
        }
    }
}
