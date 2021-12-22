package com.udpr.quot.domain.remark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.web.dto.remark.QRemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.query.QRemarkCommentQueryDto;
import com.udpr.quot.web.dto.remark.query.QRemarkTagQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkCommentQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkTagQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.remark.QRemark.remark;
import static com.udpr.quot.domain.remark.QRemarkTag.remarkTag;
import static com.udpr.quot.domain.remark.comment.QComment.comment;
import static com.udpr.quot.domain.tag.QTag.tag;
import static com.udpr.quot.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class RemarkPersonPageQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<RemarkForPersonDetailQueryDto> getRemarkListForPersonDetail(RemarkForPersonDetailSearchCondition condition, Long id){

        List<RemarkForPersonDetailQueryDto> results = queryFactory
                .select(new QRemarkForPersonDetailQueryDto(
                        remark.id, remark.content, remark.remarkDate,
                        remark.createdDate, remark.updatedDate, remark.likeCount,
                        remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                        user.id, user.nickname))
                .from(remark)
                .where(remark.person.id.eq(id)
                        .and(remark.remarkDate.year().eq(condition.getYear())))
                .leftJoin(remark.user, user)
                .orderBy(remark.remarkDate.desc())
                /*.orderBy(getOrderSpecifier(condition.getSort()).toArray(OrderSpecifier[]::new))*/
                .fetch();

        List<Long> remarkIdList = toRemarkIdList(results);

        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);
        Map<Long, List<RemarkCommentQueryDto>> commentCountMap = findCommentCountMap(remarkIdList);
        setCollections(results, tagMap, commentCountMap);

        return results;
    }

    private Map<Long, List<RemarkTagQueryDto>> findTagMap(List<Long> remarkIdList) {
        List<RemarkTagQueryDto> tagList = queryFactory
                .select(new QRemarkTagQueryDto(
                        remarkTag.remark.id, tag.id, tag.name
                ))
                .from(remarkTag)
                .leftJoin(remarkTag.tag, tag)
                .where(remarkTag.remark.id.in(remarkIdList))
                .fetch();

        return tagList.stream().collect(Collectors.groupingBy(RemarkTagQueryDto::getRemarkId));
    }

    private Map<Long, List<RemarkCommentQueryDto>> findCommentCountMap(List<Long> remarkIdList){
        List<RemarkCommentQueryDto> remarkCommentList = queryFactory
                .select(new QRemarkCommentQueryDto(
                        remark.id, comment.count()
                ))
                .from(remark)
                .leftJoin(remark.commentList, comment)
                .on(comment.status.ne(Status.DELETED))
                .where(remark.id.in(remarkIdList))
                .groupBy(remark.id)
                .fetch();

        return remarkCommentList.stream()
                .collect(Collectors.groupingBy(RemarkCommentQueryDto::getRemarkId));
    }


    private List<Long> toRemarkIdList(List<RemarkForPersonDetailQueryDto> results) {
        return results.stream()
                .map(RemarkForPersonDetailQueryDto::getRemarkId)
                .collect(Collectors.toList());
    }

    private void setCollections(List<RemarkForPersonDetailQueryDto> results,
                                Map<Long, List<RemarkTagQueryDto>> tagMap,
                                Map<Long, List<RemarkCommentQueryDto>> commentCountMap) {
        results.forEach(r -> {
            r.setRemarkTagList(tagMap.get(r.getRemarkId()));
            r.setCommentCount(commentCountMap.get(r.getRemarkId()).get(0).getCommentCount());
        });
    }

    public List<Integer> getYears(Long id){
        return queryFactory.select(remark.remarkDate.year()).distinct()
                .from(remark)
                .where(remark.person.id.eq(id))
                .orderBy(remark.remarkDate.year().desc())
                .fetch();
    }

}
