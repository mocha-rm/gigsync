package com.jhlab.gigsync.domain.board.entity;

import com.jhlab.gigsync.domain.board.type.BoardType;
import com.jhlab.gigsync.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Lob
    private String text;

    private int viewCount;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFile> files = new ArrayList<>();

    @Builder
    public Board(String title, String text, BoardType boardType) {
        this.title = title;
        this.text = text;
        this.viewCount = 0;
        this.boardType = boardType;
    }
}
