package com.jhlab.gigsync.domain.board.service;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    List<BoardFile> uploadFiles(List<MultipartFile> files);

    BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files);
}
