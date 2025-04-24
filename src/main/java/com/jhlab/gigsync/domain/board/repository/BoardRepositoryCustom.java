package com.jhlab.gigsync.domain.board.repository;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.type.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);
}
