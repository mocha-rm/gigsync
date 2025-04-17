package com.jhlab.gigsync.domain.comment.service;

import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentResponseDto createComment(Long boardId, CommentRequestDto requestDto);

    Page<CommentResponseDto> findComments(Long boardId, Pageable pageable);

    CommentResponseDto findComment(Long boardId, Long commentId);

    void updateComment(Long boardId, Long commentId, CommentRequestDto requestDto);

    void deleteComment(Long boardId, Long commentId);
}
