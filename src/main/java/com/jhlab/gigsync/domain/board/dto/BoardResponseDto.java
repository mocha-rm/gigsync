package com.jhlab.gigsync.domain.board.dto;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.type.BoardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardResponseDto {
    private final Long id;
    private final String title;
    private final String text;
    private final BoardType boardType;
    private final int viewCount;
    private List<String> fileUrls;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static BoardResponseDto toDto(Board board, List<String> fileUrls) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getText(),
                board.getBoardType(),
                board.getViewCount(),
                fileUrls,
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
