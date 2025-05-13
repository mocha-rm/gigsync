package com.jhlab.gigsync.domain.user.service;

import com.jhlab.gigsync.domain.user.dto.*;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.repository.UserRepository;
import com.jhlab.gigsync.domain.user.type.UserRole;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.UserErrorCode;
import com.jhlab.gigsync.global.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto, boolean isAdmin) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        User user = User.builder()
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .nickName(userRequestDto.getNickName())
                .build();

        if (isAdmin) {
            user.registerAdminAccount();
        }

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }

    @Override
    public UserJwtWithRefreshDto login(UserRequestDto userRequestDto) {
        User findUser = getUserFromDB(userRequestDto.getEmail());

        if (!passwordEncoder.matches(userRequestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDto.getEmail(),
                        userRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(findUser);
        String refreshToken = jwtUtil.generateRefreshToken(findUser);

        redisTemplate.opsForValue()
                .set("RT:" + findUser.getId(),
                        refreshToken,
                        jwtUtil.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS
                );

        Claims claims = jwtUtil.parseClaims(accessToken);

        UserJwtResponseDto userJwtResponseDto = UserJwtResponseDto.builder()
                .id(findUser.getId())
                .email(findUser.getEmail())
                .nickName(findUser.getNickName())
                .role(findUser.getRole().toString())
                .accessToken(accessToken)
                .exp(claims.getExpiration().toInstant().atZone(ZoneId.of("UTC")))
                .build();

        return new UserJwtWithRefreshDto(userJwtResponseDto, refreshToken);
    }

    @Override
    public void logout(String accessToken, Long userId) {
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        long expiration = jwtUtil.getTokenExpirationTime(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        String refreshKey = "RT:" + userId;
        if (redisTemplate.hasKey(refreshKey)) {
            redisTemplate.delete(refreshKey);
        }

        log.info("유저 [{}] 로그아웃: accessToken 블랙리스트 등록 및 refresh 삭제", userId);
    }

    @Override
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        Long userId = jwtUtil.getUserId(refreshToken);

        validateRefreshToken(refreshToken, userId);

        User user = getUserFromDB(userId);

        String newAccessToken = jwtUtil.generateAccessToken(user);
        Claims newClaims = jwtUtil.parseClaims(newAccessToken);

        return Map.of(
                "accessToken", newAccessToken,
                "exp", newClaims.getExpiration()
        );
    }

    @Override
    public Page<UserResponseDto> findUsers(Pageable pageable) {
        Page<User> users = userRepository.findUsers(UserRole.NORMAL, pageable);
        return users.map(UserResponseDto::toDto);
    }

    @Override
    @Cacheable(value = "user", key = "#userId")
    public UserResponseDto findUser(Long userId) {
        User user = getUserFromDB(userId);
        return UserResponseDto.toDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public void updateNickname(Long userId, UserUpdateRequestDto requestDto) {
        User user = getUserFromDB(userId);
        user.updateNickname(requestDto.getNickName());
        userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public void updatePassword(Long userId, UserUpdateRequestDto requestDto) {
        User user = getUserFromDB(userId);

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        } else if (requestDto.getCurrentPassword().equals(requestDto.getNewPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_BAD_REQUEST_SAME_AS_BEFORE);
        } else if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_BAD_REQUEST_CONFIRM_AGAIN);
        }

        user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public void deleteUser(Long userId) {
        User user = getUserFromDB(userId);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public void registerAdmin(Long userId) {
        User user = getUserFromDB(userId);
        user.registerAdminAccount();
    }

    @Override
    public User getUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserFromDB(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    private void validateRefreshToken(String refreshToken, Long userId) {
        String redisKey = "RT:" + userId;
        String storedToken = (String) redisTemplate.opsForValue().get(redisKey);

        log.info("Stored Token: {}", storedToken);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
