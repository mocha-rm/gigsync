package com.jhlab.gigsync.domain.board.dto;

import com.jhlab.gigsync.domain.board.type.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BoardRequestDto {
    private final String title;
    private final String text;
    private final BoardType boardType;
    private final List<Long> deleteFileIds;
}
