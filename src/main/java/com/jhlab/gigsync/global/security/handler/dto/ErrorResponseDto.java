package com.jhlab.gigsync.global.security.handler.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int value;
    private final String message;
    private final LocalDateTime localDateTime;
}
