package com.udpr.quot.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.udpr.quot.domain.user.QBookmark.bookmark;

@RequiredArgsConstructor
@Repository
public class BookmarkQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Long findBookmarkId(Long remarkId, Long userId){
        return queryFactory
                .select(bookmark.id)
                .from(bookmark)
                .where(bookmark.remark.id.eq(remarkId)
                        .and(bookmark.user.id.eq(userId)))
                .fetchOne();
    }

}
