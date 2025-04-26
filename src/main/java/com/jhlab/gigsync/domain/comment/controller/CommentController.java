package com.jhlab.gigsync.domain.comment.controller;

import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;
import com.jhlab.gigsync.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Operation(summary = "댓글 생성")
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long boardId,
                                                            @Valid @RequestBody CommentRequestDto requestDto) {
        return new ResponseEntity<>(commentService.createComment(boardId, requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "댓글 다건 조회")
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> findComments(@PathVariable Long boardId,
                                                                 @PageableDefault(
                                                                         sort = "createdAt",
                                                                         direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(commentService.findComments(boardId, pageable), HttpStatus.OK);
    }

    @Operation(summary = "특정 댓글 조회")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> findComment(@PathVariable Long boardId,
                                                          @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.findComment(boardId, commentId), HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long boardId,
                                                @PathVariable Long commentId,
                                                @Valid @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(boardId, commentId, requestDto);
        return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long boardId,
                                                @PathVariable Long commentId) {
        commentService.deleteComment(boardId, commentId);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }
}
