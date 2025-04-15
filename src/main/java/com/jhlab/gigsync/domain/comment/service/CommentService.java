package com.jhlab.gigsync.domain.comment.service;

import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto createComment(Long boardId, CommentRequestDto requestDto);
}
