package com.udpr.quot.domain.remark.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.web.dto.remark.query.QRemarkApiQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkApiQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.udpr.quot.domain.remark.QRemark.remark;

@RequiredArgsConstructor
@Repository
public class RemarkApiQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<RemarkApiQueryDto> getRemarkList(String category){

        return queryFactory.select(new QRemarkApiQueryDto(
                remark.id, remark.content, remark.remarkDate, remark.likeCount, remark.dislikeCount,
                remark.person.name, remark.person.job, remark.commentList.size()
        ))
                .from(remark)
                .where(remark.person.category.eq(category))
                .leftJoin(remark.person)
                .orderBy()
                .orderBy(NumberExpression.random().asc())
                .limit(5)
                .fetch();
    }

}
