package com.jhlab.gigsync.domain.board.dto;

import com.jhlab.gigsync.domain.board.type.BoardType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    private final String title;
    private final String text;
    private final BoardType boardType;

    @Builder
    public BoardRequestDto(String title, String text, BoardType boardType) {
        this.title = title;
        this.text = text;
        this.boardType = boardType;
    }
}
