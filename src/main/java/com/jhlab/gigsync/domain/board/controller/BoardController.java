package com.jhlab.gigsync.domain.board.controller;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.service.BoardService;
import com.jhlab.gigsync.domain.board.type.BoardType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestPart("board") @Valid BoardRequestDto requestDto,
                                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return new ResponseEntity<>(boardService.createBoard(requestDto, files), HttpStatus.CREATED);
    }

    @Operation(summary = "특정 게시글 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(boardService.findBoard(boardId), HttpStatus.OK);
    }

    @Operation(summary = "게시글 다건 조회")
    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> findBoards(@RequestParam(defaultValue = "latest") String sortType,
                                                             @RequestParam(required = false) BoardType boardType,
                                                             @PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(boardService.findBoardsSorted(sortType, boardType, pageable), HttpStatus.OK);
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable Long boardId,
                                              @RequestPart("board") @Valid BoardRequestDto requestDto,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        boardService.updateBoard(boardId, requestDto, files);
        return new ResponseEntity<>("게시글이 수정되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
    }
}
