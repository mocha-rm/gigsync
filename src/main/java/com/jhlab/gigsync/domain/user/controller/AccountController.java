package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.UserJwtResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserRequestDto;
import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    @Operation(summary = "관리자 계정 생성")
    @PostMapping("/signup/admin")
    public ResponseEntity<UserResponseDto> adminSignup(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto, true), HttpStatus.CREATED);
    }

    @Operation(summary = "계정 생성")
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto, false), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<UserJwtResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.login(userRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(accessToken, userDetails.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
