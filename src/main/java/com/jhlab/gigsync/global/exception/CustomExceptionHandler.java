package com.jhlab.gigsync.global.exception;

import com.jhlab.gigsync.global.exception.type.UserErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");

        String message = UserErrorCode.INVALID_USER.getMessage();
        response.getWriter().write(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");

        String message;
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("api/admin") || requestUri.startsWith("/admin")) {
            message = UserErrorCode.NEED_ADMIN_ROLE.getMessage();
        } else {
            message = UserErrorCode.USER_UNAUTHORIZED.getMessage();
        }

        response.getWriter().write(message);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(new ExceptionResponseDto(exception.getErrorCode(), exception.getMessage()), exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }
}
