package com.jhlab.gigsync.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponseDto {
    private final String errorCode;
    private final String message;
}
