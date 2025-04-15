package com.jhlab.gigsync.domain.comment.service;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.service.BoardService;
import com.jhlab.gigsync.domain.comment.dto.CommentRequestDto;
import com.jhlab.gigsync.domain.comment.dto.CommentResponseDto;
import com.jhlab.gigsync.domain.comment.entity.Comment;
import com.jhlab.gigsync.domain.comment.repository.CommentRepository;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
