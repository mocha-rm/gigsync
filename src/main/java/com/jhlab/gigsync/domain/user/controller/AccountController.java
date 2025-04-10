package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.UserJwtResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserRequestDto;
import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserJwtResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.login(userRequestDto), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
