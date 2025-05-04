package com.jhlab.gigsync.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardFileDto {
    private final Long id;
    private final String fileName;
    private final String fileUrl;

    @Builder
    @JsonCreator
    public BoardFileDto(
            @JsonProperty("id") Long id,
            @JsonProperty("fileName") String fileName,
            @JsonProperty("fileUrl") String fileUrl
    ) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public static BoardFileDto from(BoardFile boardFile) {
        return BoardFileDto.builder()
                .id(boardFile.getId())
                .fileName(boardFile.getFileName())
                .fileUrl(boardFile.getFileUrl())
                .build();
    }
}
