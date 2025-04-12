package com.jhlab.gigsync.domain.board.service;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files);

    BoardResponseDto findBoard(Long boardId);

    Page<BoardResponseDto> findBoardsSorted(String sortType, Pageable pageable);

    Board getBoardFromDB(Long boardId);
}
