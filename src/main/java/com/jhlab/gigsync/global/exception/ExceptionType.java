package com.jhlab.gigsync.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus getHttpStatus();

    String getErrorCode();

    String getMessage();
}
