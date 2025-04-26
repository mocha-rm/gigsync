package com.jhlab.gigsync.global.common.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {
    public static OrderSpecifier<?>[] getSort(Sort sort, PathBuilder<?> entityPath) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            orderSpecifiers.add(new OrderSpecifier(direction, entityPath.get(order.getProperty())));
        });
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
