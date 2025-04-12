package com.jhlab.gigsync.domain.board.controller;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestPart("board") BoardRequestDto requestDto,
                                                        @RequestPart("files") List<MultipartFile> files) {
        return new ResponseEntity<>(boardService.createBoard(requestDto, files), HttpStatus.CREATED);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(boardService.findBoard(boardId), HttpStatus.OK);
    }
}
