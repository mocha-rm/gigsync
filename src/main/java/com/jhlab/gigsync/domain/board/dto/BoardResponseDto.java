package com.jhlab.gigsync.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import com.jhlab.gigsync.domain.board.type.BoardType;
import com.jhlab.gigsync.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String text;
    private final BoardType boardType;
    private final long viewCount;
    private final List<BoardFileDto> files;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    @JsonCreator
    public BoardResponseDto(
            @JsonProperty("id") Long id,
            @JsonProperty("userId") Long userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("boardType") BoardType boardType,
            @JsonProperty("viewCount") long viewCount,
            @JsonProperty("files") List<BoardFileDto> files,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("modifiedAt") LocalDateTime modifiedAt
    ) {

        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.text = text;
        this.boardType = boardType;
        this.viewCount = viewCount;
        this.files = files;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardResponseDto toDto(Board board, User user, List<BoardFile> boardFiles) {
        List<BoardFileDto> fileDtos = boardFiles != null ?
                boardFiles.stream()
                        .map(BoardFileDto::from)
                        .toList(): new ArrayList<>();

        return new BoardResponseDto(
                board.getId(),
                user.getId(),
                user.getNickName(),
                board.getTitle(),
                board.getText(),
                board.getBoardType(),
                board.getViewCount(),
                fileDtos,
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
