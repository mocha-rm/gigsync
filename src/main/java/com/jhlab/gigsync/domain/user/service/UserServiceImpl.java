package com.jhlab.gigsync.domain.user.service;

import com.jhlab.gigsync.domain.user.dto.UserRequestDto;
import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .nickName(userRequestDto.getNickName())
                .build();

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }

    @Override
    public UserResponseDto login(UserRequestDto userRequestDto) {
        User findUser = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!findUser.getPassword().equals(userRequestDto.getPassword())) {
            throw new IllegalArgumentException("Password Not Match");
        }

        // 세션 로그인 or JWT
        return UserResponseDto.toDto(findUser);
    }

    @Override
    public void logout() {
        // JWT 구현 시 로그아웃 처리는 어떻게? Redis, Refresh Token, Black List
    }
}
