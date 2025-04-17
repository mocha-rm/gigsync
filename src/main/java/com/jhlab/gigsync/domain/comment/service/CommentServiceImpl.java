package com.jhlab.gigsync.domain.comment.service;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.service.BoardService;
import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;
import com.jhlab.gigsync.domain.comment.entity.Comment;
import com.jhlab.gigsync.domain.comment.repository.CommentRepository;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BoardService boardService;

    @Override
    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto requestDto) {
        User user = userService.getUserFromDB(SecurityContextHolder.getContext().getAuthentication().getName());
        Board board = boardService.getBoardFromDB(boardId);

        Comment comment = Comment.builder()
                .text(requestDto.getText())
                .board(board)
                .user(user)
                .build();

        commentRepository.save(comment);

        return CommentResponseDto.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> findComments(Long boardId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByBoardId(boardId, pageable);
        return commentPage.map(CommentResponseDto::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto findComment(Long boardId, Long commentId) {
        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));

        return CommentResponseDto.toDto(comment);
    }
}
