package com.jhlab.gigsync.domain.board.service;

import com.jhlab.gigsync.domain.board.dto.BoardRequestDto;
import com.jhlab.gigsync.domain.board.dto.BoardResponseDto;
import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import com.jhlab.gigsync.domain.board.repository.BoardFileRepository;
import com.jhlab.gigsync.domain.board.repository.BoardRepository;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.service.UserService;
import com.jhlab.gigsync.global.common.service.FileService;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserService userService;
    private final FileService fileService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromDB(authentication.getName());

        Board board = Board.builder()
                .title(requestDto.getTitle())
                .text(requestDto.getText())
                .boardType(requestDto.getBoardType())
                .user(user)
                .build();

        boardRepository.save(board);

        List<BoardFile> uploadedFiles = fileService.uploadFiles(files, board);
        boardFileRepository.saveAll(uploadedFiles);

        List<String> fileUrls = uploadedFiles.stream()
                .map(BoardFile::getFileUrl)
                .toList();

        return BoardResponseDto.toDto(board, user, fileUrls);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long boardId) {
        String boardDetailKey = "boardDetail:" + boardId;
        String viewKey = "board:view:" + boardId;

        BoardResponseDto cached = (BoardResponseDto) redisTemplate.opsForValue().get(boardDetailKey);
        if (cached != null) {
            log.info("캐시된 게시글 반환 - boardId: {}", boardId);

            redisTemplate.opsForValue().increment(viewKey);
            return cached;
        }

        Board board = getBoardFromDB(boardId);
        List<String> fileUrls = board.getFiles().stream()
                .map(BoardFile::getFileUrl)
                .toList();

        Long redisViewCount = redisTemplate.opsForValue().increment(viewKey);
        long totalViewCount = board.getViewCount() + (redisViewCount != null ? redisViewCount - 1 : 0);

        board.setViewCount(totalViewCount);
        BoardResponseDto dto = BoardResponseDto.toDto(board, board.getUser(), fileUrls);

        redisTemplate.opsForValue().set(boardDetailKey, dto, Duration.ofMinutes(10));

        log.info("게시글 캐시 저장 완료 - boardId: {}", boardId);

        return dto;
    }

    @Override
    public Page<BoardResponseDto> findBoardsSorted(String sortType, Pageable pageable) {
        Sort sort;

        if (sortType.equals("view")) {
            sort = Sort.by(Sort.Direction.DESC, "viewCount");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<Board> boardPage = boardRepository.findAll(sortedPageable);

        List<BoardResponseDto> dtoList = boardPage.getContent().stream()
                .map(board -> BoardResponseDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .viewCount(board.getViewCount())
                        .build()
                ).toList();

        return new PageImpl<>(dtoList, sortedPageable, boardPage.getTotalElements());
    }

    @Override
    public Board getBoardFromDB(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));
    }
}
