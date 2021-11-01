package com.udpr.quot.domain.person.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.domain.remark.comment.QComment;
import com.udpr.quot.web.dto.person.*;
import com.udpr.quot.web.dto.remark.QRemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.query.*;
import com.udpr.quot.web.dto.search.QSearchPersonResponseDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.remark.QRemark.remark;
import static com.udpr.quot.domain.remark.QRemarkTag.remarkTag;
import static com.udpr.quot.domain.remark.comment.QComment.comment;
import static com.udpr.quot.domain.tag.QTag.tag;
import static com.udpr.quot.domain.user.QUser.user;

public class PersonRepositoryImpl implements PersonRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PersonRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PersonListResponseDto> search(PersonSearchCondition condition){
        return queryFactory
                .select(new QPersonListResponseDto(
                        person.id,
                        person.name,
                        person.alias,
                        person.birth,
                        person.job,
                        person.status,
                        person.createdDate,
                        person.updatedDate,
                        person.category))
                .from(person)
                .where(nameLike(condition.getName()),
                        categoryEq(condition.getCategory()),
                        statusEq(condition.getStatus()),
                        jobLike((condition.getJob()))
                        )

                .orderBy(person.name.asc())
                .fetch();
    }

    /*@Override
    public List<SearchPersonResponseDto> findByPersonName(String keyword){
        return queryFactory
                .select(new QSearchPersonResponseDto(
                        person.id,
                        person.name,
                        person.job,
                        person.category
                ))
                .from(person)
                .where(person.name.equalsIgnoreCase(keyword)
                        .or(person.name.likeIgnoreCase(keyword + "%"))
                        .or(person.name.likeIgnoreCase("%" + keyword + "%"))
                        .or(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .or(removeCommaOnPersonAlias().likeIgnoreCase("%" + keyword + "%")))
                .orderBy(new CaseBuilder()
                        .when(person.name.equalsIgnoreCase(keyword))
                        .then(1)
                        .when(person.name.likeIgnoreCase(keyword + "%"))
                        .then(2)
                        .when(person.name.likeIgnoreCase("%" + keyword + "%"))
                        .then(3)
                        .when(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .then(4)
                        .otherwise(5).asc())
                .limit(10)
                .fetch();
    }*/

    @Override
    public Page<SearchPersonResponseDto> findByPersonName(String keyword, Pageable pageable){

        QueryResults results = queryFactory
                .select(new QSearchPersonResponseDto(
                        person.id,
                        person.name,
                        person.job,
                        person.category
                ))

                .from(person)

                .where(person.name.equalsIgnoreCase(keyword)
                        .or(person.name.likeIgnoreCase(keyword + "%"))
                        .or(person.name.likeIgnoreCase("%" + keyword + "%"))
                        .or(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .or(removeCommaOnPersonAlias().likeIgnoreCase("%" + keyword + "%")))

                .orderBy(new CaseBuilder()
                        .when(person.name.equalsIgnoreCase(keyword))
                        .then(1)
                        .when(person.name.likeIgnoreCase(keyword + "%"))
                        .then(2)
                        .when(person.name.likeIgnoreCase("%" + keyword + "%"))
                        .then(3)
                        .when(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .then(4)
                        .otherwise(5).asc())
                .orderBy(person.name.asc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetchResults();

        List<SearchPersonResponseDto> content = results.getResults();
        Long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<PersonAutoCompleteDto> personAutoComplete(String keyword){
        return queryFactory
                .select(new QPersonAutoCompleteDto(
                        person.id,
                        person.name,
                        person.job
                ))
                .from(person)
                .where(person.name.likeIgnoreCase(keyword + "%")
                .or(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                .orderBy(new CaseBuilder()
                        .when(person.name.equalsIgnoreCase(keyword))
                        .then(1)
                        .when(person.name.likeIgnoreCase(keyword + "%")
                                .or(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                        .then(2)
                        .otherwise(3).asc())
                .fetch();
    }

    @Override
    public List<PersonAutoCompleteDto> personAutoCompleteForMainSearch(String keyword){

        int limit;

        if(keyword.length()>1){
            limit=15;
        }else{
            limit=10;
        }

        return queryFactory
                .select(new QPersonAutoCompleteDto(
                        person.id,
                        person.name,
                        person.job
                ))
                .from(person)
                .where(person.name.likeIgnoreCase(keyword + "%")
                        .or(person.alias.likeIgnoreCase("%" + keyword + "%")))
                .orderBy(new CaseBuilder()
                        .when(person.name.equalsIgnoreCase(keyword))
                        .then(1)
                        .when(person.name.likeIgnoreCase("%" + keyword + "%")
                                .or(removeCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                        .then(2)
                        .otherwise(3).asc())
                .limit(limit)
                .fetch();
    }



    @Override
    public PersonQueryDto getDetail(Long id){

        PersonQueryDto result = queryFactory
                .select(new QPersonQueryDto( person.id, person.name, person.alias, person.birth,
                        person.gender, person.job, person.summary, person.category))
                .from(person)
                .where(person.id.eq(id))
                .fetchOne();

        return result;
    }

    @Override
    public List<RemarkForPersonDetailQueryDto> getRemarkListForPersonDetail(RemarkForPersonDetailSearchCondition condition, Long id){

        List<RemarkForPersonDetailQueryDto> results = queryFactory
                .select(new QRemarkForPersonDetailQueryDto(
                        remark.id, remark.content, remark.remarkDate,
                        remark.createdDate, remark.updatedDate, remark.likeCount,
                        remark.dislikeCount,remark.sourceSort, remark.sourceUrl,
                        user.id, user.nickname, comment.count()))
                .from(remark)
                .where(remark.person.id.eq(id))
                .leftJoin(remark.user, user)
                .leftJoin(comment).on(remark.id.eq(comment.remark.id).and(comment.status.ne(Status.DELETED)))
                .groupBy(remark.id)
                .orderBy(getOrderSpecifier(condition.getSort()).toArray(OrderSpecifier[]::new))
                .fetch();

        List<Long> remarkIdList = toRemarkIdList(results);

        Map<Long, List<RemarkTagQueryDto>> tagMap = findTagMap(remarkIdList);
        setTagMapsToRemark(results, tagMap);

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

        Map<Long, List<RemarkTagQueryDto>> tagMap =
                tagList.stream().collect(Collectors.groupingBy(RemarkTagQueryDto::getRemarkId));
        return tagMap;
    }


    private List<Long> toRemarkIdList(List<RemarkForPersonDetailQueryDto> results) {
        return results.stream()
                .map(RemarkForPersonDetailQueryDto::getRemarkId)
                .collect(Collectors.toList());
    }

    private void setTagMapsToRemark(List<RemarkForPersonDetailQueryDto> results, Map<Long, List<RemarkTagQueryDto>> tagMap) {
            results.forEach(r -> r.setRemarkTagList(tagMap.get(r.getRemarkId())));
    }






    private Predicate nameLike(String name) {
        if (name != null && name.length() > 0)
            return person.name.likeIgnoreCase("%" + name + "%").or(person.alias.likeIgnoreCase("%" + name + "%"));
        return null;
    }

    private Predicate jobLike(String job) {
        if (job != null && job.length() > 0)
            return person.job.like("%" + job + "%");
        return null;
    }


    private Predicate categoryEq(String category) {
        if (category != null && category.length() > 0)
            return person.category.eq(category);
        return null;
    }

    private Predicate statusEq(Status status) {
        if (status != null)
            return person.status.eq(status);
        return null;
    }

    private List<OrderSpecifier> getOrderSpecifier(String sort){

        List<OrderSpecifier> orders = new ArrayList<>();

        if(sort!=null) {
            switch (sort) {
                case "rd_d":
                    orders.add(remark.remarkDate.desc());
                    break;
                case "cd_d":
                    orders.add(remark.createdDate.asc());
                    break;
            }
        }

        orders.add(remark.remarkDate.desc());

        return orders;

    }

    public StringTemplate removeCommaOnPersonAlias() {
        StringTemplate st = Expressions.stringTemplate("replace({0},',','')", person.alias);
        return st;
    }


}
