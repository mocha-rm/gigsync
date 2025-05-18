package com.jhlab.gigsync.domain.user.service;

import com.jhlab.gigsync.domain.user.dto.*;
import com.jhlab.gigsync.domain.user.dto.auth.FindEmailRequestDto;
import com.jhlab.gigsync.domain.user.dto.auth.FindEmailResponseDto;
import com.jhlab.gigsync.domain.user.dto.auth.ResetPasswordRequestDto;
import com.jhlab.gigsync.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    UserResponseDto createUser(SignUpRequestDto requestDto, boolean isAdmin);

    UserJwtWithRefreshDto login(UserRequestDto userRequestDto);

    void logout(String accessToken, Long userId);

    Map<String, Object> refreshAccessToken(String refreshToken);

    Page<UserResponseDto> findUsers(Pageable pageable);

    UserResponseDto findUser(Long userId);

    void updateNickname(Long userId, UserUpdateRequestDto requestDto);

    void updatePassword(Long userId, UserUpdateRequestDto requestDto);

    void deleteUser(Long userId);

    void registerAdmin(Long userId);

    User getUserFromDB(Long userId);

    User getUserFromDB(String email);

    FindEmailResponseDto findEmail(FindEmailRequestDto findEmailRequestDto);

    void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
}
