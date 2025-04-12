package com.jhlab.gigsync.global.common.utils;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.repository.BoardRepository;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class BoardScheduler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void syncViewCounts() {
        log.info("조회수 동기화 시작");
        Set<String> keys = redisTemplate.keys("board:view:*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            try {
                Long boardId = Long.parseLong(key.replace("board:view:", ""));
                Number redisCount = (Number) redisTemplate.opsForValue().get(key);

                if (redisCount != null) {
                    Board board = boardRepository.findById(boardId)
                            .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));

                    board.addViewCount(redisCount.longValue());
                    boardRepository.save(board);
                    redisTemplate.delete(key);
                }
            } catch (Exception exception) {
                log.error("조회수 동기화 실패: {}", key, exception);
            }
        }
    }
}
