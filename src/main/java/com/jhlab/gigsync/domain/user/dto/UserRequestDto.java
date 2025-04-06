package com.jhlab.gigsync.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private final String email;
    private final String password;
    private final String nickName;

    @Builder
    public UserRequestDto(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
