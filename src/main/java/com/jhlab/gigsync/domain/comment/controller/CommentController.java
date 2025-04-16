package com.jhlab.gigsync.domain.comment.controller;

import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;
import com.jhlab.gigsync.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long boardId,
                                                            @RequestBody CommentRequestDto requestDto) {
        return new ResponseEntity<>(commentService.createComment(boardId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> findComments(@PathVariable Long boardId,
                                                                 @PageableDefault(
                                                                         sort = "createdAt",
                                                                         direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(commentService.findComments(boardId, pageable), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> findComment(@PathVariable Long boardId,
                                                          @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.findComment(boardId, commentId), HttpStatus.OK);
    }
}
