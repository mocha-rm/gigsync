package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.*;
import com.jhlab.gigsync.domain.user.dto.auth.FindEmailRequestDto;
import com.jhlab.gigsync.domain.user.dto.auth.FindEmailResponseDto;
import com.jhlab.gigsync.domain.user.dto.auth.ResetPasswordRequestDto;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    @Operation(summary = "관리자 계정 생성")
    @PostMapping("/signup/admin")
    public ResponseEntity<UserResponseDto> adminSignup(@RequestBody SignUpRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto, true), HttpStatus.CREATED);
    }

    @Operation(summary = "계정 생성")
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto, false), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<UserJwtResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        UserJwtWithRefreshDto tokens = userService.login(userRequestDto);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(false) //TODO: HTTPS에서는 true로 변경 필요
                .path("/api/auth/refresh")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(14))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(tokens.getJwtResponseDto());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(accessToken, userDetails.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "엑세스토큰 재발급")
    @PostMapping("/auth/refresh")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
        return new ResponseEntity<>(userService.refreshAccessToken(refreshToken), HttpStatus.OK);
    }

    @Operation(summary = "가입한 이메일 찾기")
    @GetMapping("/auth/findEmail")
    public ResponseEntity<FindEmailResponseDto> findEmail(@RequestBody FindEmailRequestDto findEmailRequestDto) {
        return new ResponseEntity<>(userService.findEmail(findEmailRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 재설정")
    @PostMapping("/auth/resetPassword")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        userService.resetPassword(resetPasswordRequestDto);
        return new ResponseEntity<>("비밀번호가 재설정되었습니다.", HttpStatus.OK);
    }
}
