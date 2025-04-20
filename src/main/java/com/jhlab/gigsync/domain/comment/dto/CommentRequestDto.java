package com.jhlab.gigsync.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 180, message = "댓글 사이즈 검증. (최소 1자 ~ 180자 제한)")
    private final String text;
}
