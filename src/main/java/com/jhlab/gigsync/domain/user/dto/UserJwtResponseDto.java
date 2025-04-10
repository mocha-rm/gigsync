package com.jhlab.gigsync.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class UserJwtResponseDto {
    private final Long id;
    private final String email;
    private final String role;
    private final String accessToken;
    private final Date exp;

    @Builder
    public UserJwtResponseDto(Long id, String email, String role, String accessToken, Date exp) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.exp = exp;
    }
}
