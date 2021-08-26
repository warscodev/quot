package com.udpr.quot.domain.remark.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.web.dto.remark.RemarkTestDto;
import com.udpr.quot.web.dto.tag.QTagDto;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.udpr.quot.domain.remark.QRemark.remark;
import static com.udpr.quot.domain.remark.QRemarkTag.remarkTag;
import static com.udpr.quot.domain.person.QPerson.person;
import static org.springframework.util.ObjectUtils.isEmpty;
import static com.udpr.quot.domain.tag.QTag.tag;


public class RemarkRepositoryImpl implements RemarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RemarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Remark> searchAll(Pageable pageable) {
        QueryResults<Remark> results = queryFactory
                .selectFrom(remark)
                .leftJoin(remark.person).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(remark.remarkDate.desc(), remark.createdDate.desc())
                .fetchResults();

        List<Remark> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<TagDto> getTags(Long remarkId) {
        return queryFactory
                .select(new QTagDto(
                        tag.id,
                        tag.name))
                .from(remarkTag)
                .join(tag)
                .on(remarkTag.remark.id.eq(remarkId),
                        remarkTag.tag.id.eq(tag.id))
                .orderBy(remarkTag.id.asc())
                .fetch();
    }

    @Override
    public Page<Remark> searchByContentOrPersonName(String searchKeyword, Pageable pageable) {
        QueryResults<Remark> results = queryFactory
                .selectFrom(remark)
                .join(remark.person, person).fetchJoin()
                .where(keywordLike(searchKeyword)
                        .or(person.name.likeIgnoreCase("%" + searchKeyword + "%"))
                        .or(person.alias.likeIgnoreCase("%" + searchKeyword + "%")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(remark.remarkDate.desc(), remark.createdDate.desc())
                .fetchResults();

        List<Remark> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Remark> searchByPersonName(String searchKeyword, Long personId, Pageable pageable) {
        QueryResults<Remark> results = queryFactory
                .selectFrom(remark)
                .join(remark.person, person).fetchJoin()
                .where(person.name.likeIgnoreCase("%" + searchKeyword + "%")
                        .or(person.alias.likeIgnoreCase("%" + searchKeyword + "%"))
                        .and(personIdEq(personId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(remark.remarkDate.desc(), remark.createdDate.desc())
                .fetchResults();


        List<Remark> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Remark> searchByTagName(String searchKeyword, Pageable pageable) {
        QueryResults<Remark> results = queryFactory
                .select(remark)
                .from(remarkTag)
                .join(remarkTag.remark, remark)
                .on(remarkTag.tag.name.eq(searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(remark.remarkDate.desc(), remark.createdDate.desc())
                .fetchResults();

        List<Remark> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
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



}
