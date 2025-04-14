package com.jhlab.gigsync.domain.board.entity;

import com.jhlab.gigsync.domain.board.type.BoardType;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    private long viewCount;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFile> files = new ArrayList<>();

    @Builder
    public Board(String title, String text, BoardType boardType, User user) {
        this.title = title;
        this.text = text;
        this.viewCount = 0;
        this.boardType = boardType;
        this.user = user;
    }

    public void addViewCount(long count) {
        this.viewCount += count;
    }
}
