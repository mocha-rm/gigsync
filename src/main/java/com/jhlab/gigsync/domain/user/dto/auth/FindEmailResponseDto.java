package com.jhlab.gigsync.domain.user.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindEmailResponseDto {
    private final String email;
}
