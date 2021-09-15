package com.udpr.quot.domain.remark.repository;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.web.dto.remark.query.*;
import com.udpr.quot.web.dto.tag.QTagDto;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.remark.QRemark.remark;
import static com.udpr.quot.domain.remark.QRemarkTag.remarkTag;
import static com.udpr.quot.domain.remark.QRemarkLike.remarkLike;
import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.user.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;
import static com.udpr.quot.domain.tag.QTag.tag;


public class RemarkRepositoryImpl implements RemarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RemarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<RemarkQueryDto> searchAll(RemarkSearchCondition condition, Pageable pageable) {
        QueryResults<RemarkQueryDto> results = queryFactory
                        .select(new QRemarkQueryDto(
                                remark.id, remark.content, remark.remarkDate,
                                remark.createdDate, remark.updatedDate, remark.likeCount,
                                remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                                person.id, person.name, person.job, person.category,
                                user.id, user.nickname, user.picture))
                        .from(remark)
                        .leftJoin(remark.person, person)
                        .leftJoin(remark.user, user)

                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(getOrderSpecifier(condition.getSort()).stream().toArray(OrderSpecifier[]::new))
                        .fetchResults();

        List<Long> remarkIdList = toRemarkIdList(results);
        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);

        setCollections(condition, results, remarkIdList, tagMap);

        List<RemarkQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<RemarkQueryDto> searchByContentOrPersonName(RemarkSearchCondition condition, Pageable pageable) {

        QueryResults<RemarkQueryDto> results = queryFactory
                        .select(new QRemarkQueryDto(
                                remark.id, remark.content, remark.remarkDate,
                                remark.createdDate, remark.updatedDate, remark.likeCount,
                                remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                                person.id, person.name, person.job, person.category,
                                user.id, user.nickname, user.picture))
                        .from(remark)
                        .join(remark.person, person)
                        .join(remark.user, user)
                        .where(keywordLike(condition.getKeyword())
                                .or(person.name.likeIgnoreCase("%" + condition.getKeyword() + "%"))
                                .or(person.alias.likeIgnoreCase("%" + condition.getKeyword() + "%")))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(getOrderSpecifier(condition.getSort()).stream().toArray(OrderSpecifier[]::new))
                        .fetchResults();

        List<Long> remarkIdList = toRemarkIdList(results);

        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);
        setCollections(condition, results, remarkIdList, tagMap);

        List<RemarkQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public Page<RemarkQueryDto> searchByPersonName(RemarkSearchCondition condition, Pageable pageable) {

        QueryResults<RemarkQueryDto> results = queryFactory
                        .select(new QRemarkQueryDto(
                                remark.id, remark.content, remark.remarkDate,
                                remark.createdDate, remark.updatedDate, remark.likeCount,
                                remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                                person.id, person.name, person.job, person.category,
                                user.id, user.nickname, user.picture))
                        .from(remark)
                        .join(remark.person, person)
                        .join(remark.user, user)
                        .where(person.name.likeIgnoreCase("%" + condition.getKeyword() + "%")
                                .or(person.alias.likeIgnoreCase("%" + condition.getKeyword() + "%"))
                                .and(personIdEq(condition.getPersonId())))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(getOrderSpecifier(condition.getSort()).stream().toArray(OrderSpecifier[]::new))
                        .fetchResults();

        List<Long> remarkIdList = toRemarkIdList(results);

        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);
        setCollections(condition, results, remarkIdList, tagMap);

        List<RemarkQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<RemarkQueryDto> searchByTagName(RemarkSearchCondition condition, Pageable pageable) {

        QueryResults<RemarkQueryDto> results = queryFactory
                .select(new QRemarkQueryDto(
                        remark.id, remark.content, remark.remarkDate,
                        remark.createdDate, remark.updatedDate, remark.likeCount,
                        remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                        person.id, person.name, person.job, person.category,
                        user.id, user.nickname, user.picture))
                .from(remarkTag)
                .join(remarkTag.remark, remark)
                .join(remark.person, person)
                .join(remark.user, user)
                .on(remarkTag.tag.name.eq(condition.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(condition.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetchResults();

        List<Long> remarkIdList = toRemarkIdList(results);

        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);
        setCollections(condition, results, remarkIdList, tagMap);

        List<RemarkQueryDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
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

        Map<Long, List<RemarkTagQueryDto>> tagMap =
                tagList.stream().collect(Collectors.groupingBy(RemarkTagQueryDto::getRemarkId));
        return tagMap;
    }

    private Map<Long, List<RemarkLikeQueryDto>> findLikeMap(List<Long> remarkIdList, Long sid) {
        List<RemarkLikeQueryDto> likeList = queryFactory
                .select(new QRemarkLikeQueryDto(
                        remarkLike.remark.id, remarkLike.isLike
                ))
                .from(remarkLike)
                .where(remarkLike.remark.id.in(remarkIdList).and(remarkLike.user.id.eq(sid)))
                .fetch();

        Map<Long, List<RemarkLikeQueryDto>> likeMap =
                likeList.stream().collect(Collectors.groupingBy(RemarkLikeQueryDto::getRemarkId));
        return likeMap;
    }

    private List<Long> toRemarkIdList(QueryResults<RemarkQueryDto> results) {
        List<Long> remarkIdList = results.getResults().stream()
                .map(r->r.getRemarkId())
                .collect(Collectors.toList());
        return remarkIdList;
    }

    private void setCollections(RemarkSearchCondition condition, QueryResults<RemarkQueryDto> results, List<Long> remarkIdList, Map<Long, List<RemarkTagQueryDto>> tagMap) {
        if(condition.getSid()!=null){
            Map<Long, List<RemarkLikeQueryDto>> likeMap = findLikeMap(remarkIdList, condition.getSid());
            results.getResults().forEach(r -> {
                r.setRemarkTagList(tagMap.get(r.getRemarkId()));
                if(likeMap.get(r.getRemarkId())!=null) {
                    r.setIsLike(likeMap.get(r.getRemarkId()).get(0).getIsLike());
                }
            });
        }else{
            results.getResults().forEach(r -> {
                r.setRemarkTagList(tagMap.get(r.getRemarkId()));
            });
        }
    }



    private BooleanExpression personIdEq(Long personId) {
        return isEmpty(personId) || personId == 0L ? null : remark.person.id.eq(personId);
    }

    private BooleanBuilder keywordLike(String keyword) {

        String[] keywords = keyword.split(" ");

        BooleanBuilder builder = new BooleanBuilder();

        if (keywords.length == 1) {
            System.out.println("keywords 길이 1");
            builder.or(remark.content.likeIgnoreCase("%" + keyword + "%"));
            return builder;
        } else {
            System.out.println("keywords 길이 2이상");

            Arrays.stream(keywords).forEach(r -> builder.and(remark.content.likeIgnoreCase("%" + r + "%")));

            return builder;
        }
    }



    private BooleanExpression personNameLike(String name) {
        return isEmpty(name) ? null : remark.person.name.like("%" + name + "%");
    }

    private BooleanExpression contentLike(String content) {
        return isEmpty(content) ? null : remark.content.like("%" + content + "%");
    }

    private List<OrderSpecifier> getOrderSpecifier(String sort){

        /*PathBuilder pathBuilder = new PathBuilder(remark.getType(),
                remark.getMetadata())*/;

        List<OrderSpecifier> orders = new ArrayList<>();

        switch (sort){
            case "rm_d":
                orders.add(remark.remarkDate.desc());
                break;
            case "rm_a":
                orders.add(remark.remarkDate.asc());
                break;
        }

        orders.add(remark.createdDate.desc());

        return orders;

    }



}
