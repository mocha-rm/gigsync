package com.jhlab.gigsync.domain.user.service;

import com.jhlab.gigsync.domain.user.dto.*;
import com.jhlab.gigsync.domain.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto, boolean isAdmin);

    UserJwtWithRefreshDto login(UserRequestDto userRequestDto);

    void logout(String accessToken, Long userId);

    Map<String, Object> refreshAccessToken(String refreshToken);

    List<UserResponseDto> findUsers();

    UserResponseDto findUser(Long userId);

    void updateNickname(Long userId, UserUpdateRequestDto requestDto);

    void updatePassword(Long userId, UserUpdateRequestDto requestDto);

    void deleteUser(Long userId);

    void registerAdmin(Long userId);

    User getUserFromDB(Long userId);

    User getUserFromDB(String email);
}
