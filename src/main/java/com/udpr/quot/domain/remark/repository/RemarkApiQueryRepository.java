package com.udpr.quot.domain.remark.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.web.dto.remark.query.QRemarkApiQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkApiQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.remark.QRemark.remark;

@RequiredArgsConstructor
@Repository
public class RemarkApiQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<RemarkApiQueryDto> getRemarkList(String category){

        List<RemarkApiQueryDto> results = queryFactory.select(new QRemarkApiQueryDto(
                        remark.id, remark.content, remark.remarkDate, remark.likeCount, remark.dislikeCount,
                        person.name, person.job, remark.commentCount
                ))
                .from(remark)
                .leftJoin(remark.person, person)
                .where(person.category.eq(category)
                        .and(remark.createdDate.between(LocalDateTime.now().minusDays(14),LocalDateTime.now()))
                        .and(remark.likeCount.add(remark.dislikeCount).add(remark.commentCount).goe(1)))
                .orderBy(remark.likeCount.add(remark.dislikeCount).add(remark.commentCount).desc())
                .orderBy(remark.commentCount.desc())
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                /*.orderBy(NumberExpression.random().asc())*/
                .limit(5)
                .fetch();

                List<Long> remarkIdList = results.stream()
                        .map(RemarkApiQueryDto::getRemarkId)
                        .collect(Collectors.toList());

/*        List<RemarkCommentQueryDto> remarkCommentList = queryFactory
                .select(new QRemarkCommentQueryDto(
                        remark.id, comment.count()
                ))
                .from(remark)
                .leftJoin(remark.commentList, comment)
                    .on(comment.status.ne(Status.DELETED))
                .where(remark.id.in(remarkIdList))
                .groupBy(remark.id)
                .fetch();


        Map<Long, List<RemarkCommentQueryDto>> commentCountMap = remarkCommentList.stream()
                .collect(Collectors.groupingBy(RemarkCommentQueryDto::getRemarkId));

        results.forEach(r->{
            r.setCommentCount(commentCountMap.get(r.getRemarkId()).get(0).getCommentCount());
        });*/

        return results;
    }

}
