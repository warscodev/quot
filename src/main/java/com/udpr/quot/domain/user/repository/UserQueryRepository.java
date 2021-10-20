package com.udpr.quot.domain.user.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.web.dto.user.QUserQueryDto;
import com.udpr.quot.web.dto.user.UserQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.udpr.quot.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryDto findById(Long userId){
        return queryFactory
                .select(new QUserQueryDto(
                user.id, user.email, user.nickname, user.createdDate, user.updatedDate))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }

}
