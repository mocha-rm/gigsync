package com.jhlab.gigsync.domain.user.controller;

import com.jhlab.gigsync.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users/{userId}")
public class AdminUserController {
    private final UserService userService;

    @PutMapping("/admin_registration")
    public ResponseEntity<String> registerAdmin(@PathVariable Long userId) {
        userService.registerAdmin(userId);
        return new ResponseEntity<>("관리자 등록이 완료되었습니다.", HttpStatus.OK);
    }

    @PutMapping("/ban")
    public ResponseEntity<String> banUser(@PathVariable Long userId) {
        //userService.banUser(userId);
        return new ResponseEntity<>("유저 벤 처리가 완료되었습니다.", HttpStatus.OK);
    }
}
