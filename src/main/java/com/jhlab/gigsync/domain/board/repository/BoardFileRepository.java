package com.jhlab.gigsync.domain.board.repository;

import com.jhlab.gigsync.domain.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
