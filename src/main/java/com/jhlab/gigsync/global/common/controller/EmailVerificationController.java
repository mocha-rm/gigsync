package com.jhlab.gigsync.global.common.controller;

import com.jhlab.gigsync.domain.user.dto.verification.EmailVerificationRequestDto;
import com.jhlab.gigsync.global.common.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService verificationService;

    @PostMapping("/api/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequestDto requestDto) throws MessagingException {
        verificationService.sendVerificationCode(requestDto.getEmail());
        return new ResponseEntity<>("인증 코드가 전송되었습니다.", HttpStatus.OK);
    }

}
