package com.jhlab.gigsync.domain.user.service;

import com.jhlab.gigsync.domain.user.dto.UserJwtResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserRequestDto;
import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserUpdateRequestDto;
import com.jhlab.gigsync.domain.user.entity.User;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserJwtResponseDto login(UserRequestDto userRequestDto);

    void logout(String accessToken, Long userId);

    UserResponseDto findUser(Long userId);

    void updateNickname(Long userId, UserUpdateRequestDto requestDto);

    void updatePassword(Long userId, UserUpdateRequestDto requestDto);

    void deleteUser(Long userId);

    User getUserFromDB(Long userId);

    User getUserFromDB(String email);
}
