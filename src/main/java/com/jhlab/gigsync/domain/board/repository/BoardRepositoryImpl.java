package com.jhlab.gigsync.domain.board.repository;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.QBoard;
import com.jhlab.gigsync.domain.board.type.BoardType;
import com.jhlab.gigsync.domain.user.entity.QUser;
import com.jhlab.gigsync.global.common.utils.QueryDslUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        JPQLQuery<Board> query = queryFactory
                .selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .where(boardTypeEq(boardType));

        long total = query.fetch().size();
        List<Board> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getSort(pageable.getSort(), new PathBuilder<>(Board.class, "board")))
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression boardTypeEq(BoardType boardType) {
        return boardType != null ? QBoard.board.boardType.eq(boardType) : null;
    }
}
