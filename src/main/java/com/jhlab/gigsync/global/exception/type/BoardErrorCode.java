package com.jhlab.gigsync.global.exception.type;

import com.jhlab.gigsync.global.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ExceptionType {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "수정 및 삭제는 작성자만 가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
