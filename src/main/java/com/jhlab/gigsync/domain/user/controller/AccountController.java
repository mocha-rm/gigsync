package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.UserJwtResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserRequestDto;
import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
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

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserJwtResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.login(userRequestDto), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(accessToken, userDetails.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
