package com.jhlab.gigsync.domain.comment.dto;

import com.jhlab.gigsync.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final Long boardId;
    private final String userName;
    private final String text;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getBoard().getId(),
                comment.getUser().getNickName(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
