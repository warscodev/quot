package com.udpr.quot.domain.remark.comment.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.web.dto.remark.comment.CommentQueryDto;
import com.udpr.quot.web.dto.remark.comment.QCommentQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkTagQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.remark.comment.QComment.comment;
import static com.udpr.quot.domain.user.QUser.user;

public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    final Coalesce<Long> coalesce =
            new Coalesce<>(Long.class).add(comment.ancestor.id).add(comment.id);

    @Override
    public Long getCommentCount(Long remarkId){
        return queryFactory.select(comment.count())
                .from(comment)
                .leftJoin(comment.remark)
                .where(comment.remark.id.eq(remarkId)
                        .and(comment.status.ne(Status.DELETED)))
                .fetchCount();
    }

    @Override
    public Page<CommentQueryDto> getComments(Long remarkId, Pageable pageable){
        List<CommentQueryDto> results = queryFactory
                .select(new QCommentQueryDto(
                    comment.id, comment.content, comment.createdDate, comment.updatedDate,
                        comment.status, comment.ancestor.id, comment.parent.id, comment.parent.user.nickname,
                        user.id, user.nickname))
                .from(comment)
                .leftJoin(comment.user, user)
                .leftJoin(comment.ancestor)
                .leftJoin(comment.parent)
                .leftJoin(comment.parent.user)
                .where(comment.remark.id.eq(remarkId)
                        .and(comment.status.ne(Status.DELETED)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(coalesce.asc(),
                        comment.createdDate.asc()
                )
                .fetch();
        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Long checkChildren(Long ancestorId){
        return queryFactory.select(comment.count())
                .from(comment)
                .leftJoin(comment.ancestor)
                .where(comment.ancestor.id.eq(ancestorId)
                        .and(comment.status.ne(Status.DELETED)))
                .fetchCount();
    }




}
