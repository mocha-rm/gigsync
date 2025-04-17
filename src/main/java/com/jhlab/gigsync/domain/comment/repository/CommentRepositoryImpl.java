package com.jhlab.gigsync.domain.comment.repository;

import com.jhlab.gigsync.domain.comment.entity.Comment;
import com.jhlab.gigsync.domain.comment.entity.QComment;
import com.jhlab.gigsync.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findByIdAndBoardId(Long commentId, Long boardId) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return Optional.ofNullable(
                queryFactory.selectFrom(comment)
                        .leftJoin(comment.user, user).fetchJoin()
                        .where(
                                comment.id.eq(commentId),
                                comment.board.id.eq(boardId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<Comment> findByBoardId(Long boardId, Pageable pageable) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.user, user).fetchJoin()
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetchOne();

        return new PageImpl<>(comments, pageable, total == null ? 0 : total);
    }
}
