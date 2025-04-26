package com.jhlab.gigsync.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.type.BoardType;
import com.jhlab.gigsync.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private final Long id;
    private final String userName;
    private final String title;
    private final String text;
    private final BoardType boardType;
    private final long viewCount;
    private final List<String> fileUrls;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    @JsonCreator
    public BoardResponseDto(
            @JsonProperty("id") Long id,
            @JsonProperty("userName") String userName,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("boardType") BoardType boardType,
            @JsonProperty("viewCount") long viewCount,
            @JsonProperty("fileUrls") List<String> fileUrls,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("modifiedAt") LocalDateTime modifiedAt
    ) {

        this.id = id;
        this.userName = userName;
        this.title = title;
        this.text = text;
        this.boardType = boardType;
        this.viewCount = viewCount;
        this.fileUrls = fileUrls;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardResponseDto toDto(Board board, User user, List<String> fileUrls) {
        return new BoardResponseDto(
                board.getId(),
                user.getNickName(),
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
