package com.jhlab.gigsync.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private final String nickName;
    private final String currentPassword;
    private final String newPassword;
    private final String confirmPassword;

    @Builder
    public UserUpdateRequestDto(String nickName, String currentPassword, String newPassword, String confirmPassword) {
        this.nickName = nickName;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
