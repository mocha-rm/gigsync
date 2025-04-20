package com.jhlab.gigsync.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "최소 2자에서 최대 15자까지 입력할 수 있습니다.")
    private final String nickName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String currentPassword;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 최대 20자 까지 입력할 수 있습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "대문자, 소문자, 숫자, 특수문자를 최소 1개 이상 포함해주세요.")
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
