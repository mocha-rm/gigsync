package com.jhlab.gigsync.global.exception.type;

import com.jhlab.gigsync.global.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ExceptionType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일 주소입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_BAD_REQUEST_SAME_AS_BEFORE(HttpStatus.BAD_REQUEST, "같은 비밀번호로는 변경하실 수 없습니다."),
    PASSWORD_BAD_REQUEST_CONFIRM_AGAIN(HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인란이 동일하지 않습니다."),
    INVALID_USER(HttpStatus.UNAUTHORIZED, "인증이 유효하지 않습니다, 로그인해주세요."),
    NEED_ADMIN_ROLE(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증번호가 맞지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
