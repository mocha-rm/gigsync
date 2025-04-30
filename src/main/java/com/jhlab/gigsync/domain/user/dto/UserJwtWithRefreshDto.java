package com.jhlab.gigsync.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJwtWithRefreshDto {
    private UserJwtResponseDto jwtResponseDto;
    private String refreshToken;
}
