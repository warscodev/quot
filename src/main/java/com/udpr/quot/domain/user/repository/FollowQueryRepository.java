package com.udpr.quot.domain.user.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.web.dto.search.QSearchPersonResponseDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.user.QFollow.follow;
import static com.udpr.quot.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class FollowQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<SearchPersonResponseDto> findFollowerList(Long userId, Pageable pageable){
        QueryResults<SearchPersonResponseDto> results = queryFactory
                .select(new QSearchPersonResponseDto(
                        person.id,
                        person.name,
                        person.job,
                        person.category
                ))
                .from(follow)
                .where(follow.user.id.eq(userId))
                .leftJoin(person).on(follow.person.id.eq(person.id))
                .orderBy(person.name.asc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetchResults();

        List<SearchPersonResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
