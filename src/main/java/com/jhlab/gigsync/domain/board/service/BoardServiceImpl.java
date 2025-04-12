package com.jhlab.gigsync.domain.board.service;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import com.jhlab.gigsync.domain.board.repository.BoardFileRepository;
import com.jhlab.gigsync.domain.board.repository.BoardRepository;
import com.jhlab.gigsync.global.common.service.FileService;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final FileService fileService;


    @Override
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files) {
        Board board = Board.builder()
                .title(requestDto.getTitle())
                .text(requestDto.getText())
                .boardType(requestDto.getBoardType())
                .build();

        boardRepository.save(board);

        List<BoardFile> uploadedFiles = fileService.uploadFiles(files, board);
        boardFileRepository.saveAll(uploadedFiles);

        List<String> fileUrls = uploadedFiles.stream()
                .map(BoardFile::getFileUrl)
                .toList();

        return BoardResponseDto.toDto(board, fileUrls);
    }

    @Override
    @Transactional
    public BoardResponseDto findBoard(Long boardId) {
        Board board = getBoardFromDB(boardId);

        board.addViewCount(); //TODO : Redis 캐시를 활용한 비동기처리 필요

        List<String> fileUrls = board.getFiles().stream()
                .map(BoardFile::getFileUrl)
                .toList();

        return BoardResponseDto.toDto(board, fileUrls);
    }

    @Override
    public Board getBoardFromDB(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));
    }
}
