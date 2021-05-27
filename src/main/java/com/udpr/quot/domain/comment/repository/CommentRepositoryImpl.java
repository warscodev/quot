package com.udpr.quot.domain.comment.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.web.dto.tag.QTagDto;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.udpr.quot.domain.comment.QComment.comment;
import static com.udpr.quot.domain.comment.QCommentTag.commentTag;
import static com.udpr.quot.domain.person.QPerson.person;
import static org.springframework.util.ObjectUtils.isEmpty;
import static com.udpr.quot.domain.tag.QTag.tag;


public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Comment> searchAll(Pageable pageable) {
        QueryResults<Comment> results = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.person).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.commentDate.desc())
                .fetchResults();

        List<Comment> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<TagDto> getTags(Long commentId) {

        return queryFactory
                .select(new QTagDto(
                        tag.id,
                        tag.name))
                .from(commentTag)
                .join(tag)
                .on(commentTag.comment.id.eq(commentId),
                        commentTag.tag.id.eq(tag.id))
                .orderBy(commentTag.id.asc())
                .fetch();

    }

    @Override
    public Page<Comment> searchByContentOrPersonName(String searchKeyword, Pageable pageable) {
        QueryResults<Comment> results = queryFactory
                .selectFrom(comment)
                .join(comment.person, person).fetchJoin()
                .where(comment.content.like("%" + searchKeyword + "%")
                .or(person.name.like("%" + searchKeyword + "%")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.commentDate.desc())
                .fetchResults();

        List<Comment> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Comment> searchByPersonName(String searchKeyword, Long personId, Pageable pageable) {
        QueryResults<Comment> results = queryFactory
                .selectFrom(comment)
                .join(comment.person, person).fetchJoin()
                .where(person.name.like("%" + searchKeyword + "%")
                .and(personIdEq(personId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.commentDate.desc())
                .fetchResults();

        List<Comment> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Comment> searchByTagName(String searchKeyword, Pageable pageable) {
        QueryResults<Comment> results = queryFactory
                .select(comment)
                .from(commentTag)
                .join(commentTag.comment, comment)
                .on(commentTag.tag.name.eq(searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.commentDate.desc())
                .fetchResults();

        List<Comment> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression personIdEq(Long personId) {
        return isEmpty(personId) || personId == 0L ? null : comment.person.id.eq(personId);
    }

    private BooleanExpression personNameLike(String name) {
        return isEmpty(name) ? null : comment.person.name.like("%" + name + "%");
    }

    private BooleanExpression contentLike(String content) {
        return isEmpty(content) ? null : comment.content.like("%" + content + "%");
    }


}
