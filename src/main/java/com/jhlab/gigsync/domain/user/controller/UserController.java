package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.dto.UserUpdateRequestDto;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 다건 조회")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findUsers() {
        return new ResponseEntity<>(userService.findUsers(), HttpStatus.OK);
    }

    @Operation(summary = "특정 유저 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "닉네임 변경")
    @PutMapping("/profile/name")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UserUpdateRequestDto requestDto) {
        userService.updateNickname(userDetails.getUser().getId(), requestDto);
        return new ResponseEntity<>("닉네임이 변경되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("/profile/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UserUpdateRequestDto requestDto) {
        userService.updatePassword(userDetails.getUser().getId(), requestDto);
        return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/deactivate")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return new ResponseEntity<>("탈퇴처리가 완료되었습니다.", HttpStatus.OK);
    }
}
