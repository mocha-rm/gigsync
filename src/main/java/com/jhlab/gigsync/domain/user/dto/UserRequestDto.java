package com.jhlab.gigsync.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$", message = "올바르지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 최대 20자 까지 입력할 수 있습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "대문자, 소문자, 숫자, 특수문자를 최소 1개 이상 포함해주세요.")
    private final String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "최소 2자에서 최대 15자까지 입력할 수 있습니다.")
    private final String nickName;

    @Builder
    public UserRequestDto(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
