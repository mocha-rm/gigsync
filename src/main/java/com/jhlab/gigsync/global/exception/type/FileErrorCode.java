package com.jhlab.gigsync.global.exception.type;

import com.jhlab.gigsync.global.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ExceptionType {
    UNSUPPORTED_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
    FILE_EXTENSION_MISSING(HttpStatus.BAD_REQUEST, "파일 이름에 확장자가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
