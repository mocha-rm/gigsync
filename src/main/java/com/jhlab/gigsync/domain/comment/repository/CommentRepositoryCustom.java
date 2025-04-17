package com.jhlab.gigsync.domain.comment.repository;

import com.jhlab.gigsync.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> findByIdAndBoardId(Long commentId, Long boardId);
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);
}
