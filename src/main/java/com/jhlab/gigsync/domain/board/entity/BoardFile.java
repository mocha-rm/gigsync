package com.jhlab.gigsync.domain.board.entity;

import com.jhlab.gigsync.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFile(String fileName, String fileUrl, Board board) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.board = board;
    }
}
