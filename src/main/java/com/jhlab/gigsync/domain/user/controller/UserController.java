package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserUpdateRequestDto;
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
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    @PutMapping("/profile/name")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UserUpdateRequestDto requestDto) {
        userService.updateNickname(userDetails.getUser().getId(), requestDto);
        return new ResponseEntity<>("닉네임이 변경되었습니다.", HttpStatus.OK);
    }

    @PatchMapping("/profile/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UserUpdateRequestDto requestDto) {
        userService.updatePassword(userDetails.getUser().getId(), requestDto);
        return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return new ResponseEntity<>("탈퇴처리가 완료되었습니다.", HttpStatus.OK);
    }
}
