package com.jhlab.gigsync.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private final String content;
}
