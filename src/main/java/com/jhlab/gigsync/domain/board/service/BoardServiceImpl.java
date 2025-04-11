package com.jhlab.gigsync.domain.board.service;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import com.jhlab.gigsync.domain.board.repository.BoardFileRepository;
import com.jhlab.gigsync.domain.board.repository.BoardRepository;
import com.jhlab.gigsync.global.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final FileService fileService;


    @Override
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
}
