package com.jhlab.gigsync.domain.user.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindEmailRequestDto {
    @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    private final String phoneNumber;
}
