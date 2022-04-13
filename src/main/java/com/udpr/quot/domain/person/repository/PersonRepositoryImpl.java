package com.udpr.quot.domain.person.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.web.dto.person.*;
import com.udpr.quot.web.dto.remark.QRemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.remark.query.QRemarkTagQueryDto;
import com.udpr.quot.web.dto.remark.query.RemarkTagQueryDto;
import com.udpr.quot.web.dto.search.QSearchPersonResponseDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.person.icon.QIcon.icon;
import static com.udpr.quot.domain.remark.QRemark.remark;
import static com.udpr.quot.domain.remark.QRemarkTag.remarkTag;
import static com.udpr.quot.domain.remark.comment.QComment.comment;
import static com.udpr.quot.domain.tag.QTag.tag;
import static com.udpr.quot.domain.user.QFollow.follow;
import static com.udpr.quot.domain.user.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

public class PersonRepositoryImpl implements PersonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PersonRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AdminPersonListResponseDto> search(PersonSearchCondition condition) {
        return queryFactory
                .select(new QAdminPersonListResponseDto(
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

    @Override
    public Page<SearchPersonResponseDto> findByPersonName(String keyword, Pageable pageable) {

        QueryResults<SearchPersonResponseDto> results = queryFactory
                .select(new QSearchPersonResponseDto(
                        person.id,
                        person.name,
                        person.job,
                        person.category
                ))

                .from(person)

                .where(replaceSpacePersonName().equalsIgnoreCase(keyword)
                        .or(replaceSpacePersonName().likeIgnoreCase(keyword + "%"))
                        .or(replaceSpacePersonName().likeIgnoreCase("%" + keyword + "%"))
                        .or(replaceCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .or(replaceCommaOnPersonAlias().likeIgnoreCase("%" + keyword + "%"))
                        .or(person.organization.like("%" + keyword + "%"))
                )


                .orderBy(new CaseBuilder()
                        .when(replaceSpacePersonName().equalsIgnoreCase(keyword))
                        .then(1)
                        .when(replaceSpacePersonName().likeIgnoreCase(keyword + "%"))
                        .then(2)
                        .when(replaceSpacePersonName().likeIgnoreCase("%" + keyword + "%"))
                        .then(3)
                        .when(replaceCommaOnPersonAlias().likeIgnoreCase(keyword + "%"))
                        .then(4)
                        .otherwise(5).asc())
                .orderBy(person.name.asc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetchResults();

        List<SearchPersonResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<PersonAutoCompleteDto> personAutoComplete(String keyword) {
        return queryFactory
                .select(new QPersonAutoCompleteDto(
                        person.id,
                        person.name,
                        person.job
                ))
                .from(person)
                .where(replaceSpacePersonName().likeIgnoreCase(keyword + "%")
                        .or(replaceCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                .orderBy(new CaseBuilder()
                        .when(replaceSpacePersonName().equalsIgnoreCase(keyword))
                        .then(1)
                        .when(replaceSpacePersonName().likeIgnoreCase(keyword + "%")
                                .or(replaceCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                        .then(2)
                        .otherwise(3).asc())
                .fetch();
    }

    @Override
    public List<PersonAutoCompleteDto> personAutoCompleteForMainSearch(String keyword) {
        int limit;
        if (keyword.length() > 1) {
            limit = 15;
        } else {
            limit = 10;
        }
        return queryFactory
                .select(new QPersonAutoCompleteDto(
                        person.id,
                        person.name,
                        person.job
                ))
                .from(person)
                .where(replaceSpacePersonName().likeIgnoreCase(keyword + "%")
                        .or(person.alias.likeIgnoreCase("%" + keyword + "%")))
                .orderBy(new CaseBuilder()
                        .when(replaceSpacePersonName().equalsIgnoreCase(keyword))
                        .then(1)
                        .when(replaceSpacePersonName().likeIgnoreCase("%" + keyword + "%")
                                .or(replaceCommaOnPersonAlias().likeIgnoreCase(keyword + "%")))
                        .then(2)
                        .otherwise(3).asc())
                .limit(limit)
                .fetch();
    }

    @Override
    public PersonQueryDto getDetail(Long id) {
        return queryFactory
                .select(new QPersonQueryDto(person.id, person.name, person.alias, person.birth,
                        person.gender, person.job, person.summary, person.category, person.organization, person.image,
                        person.createdDate, person.updatedDate,
                        icon.id, icon.path))
                .from(person)
                .leftJoin(person.icon, icon)
                .where(person.id.eq(id))
                .fetchOne();
    }

    @Override
    public PersonQueryDto getDetail(Long personId, Long userId) {
        return queryFactory
                .select(new QPersonQueryDto(person.id, person.name, person.alias, person.birth,
                        person.gender, person.job, person.summary, person.category, follow.id, person.organization, person.image,
                        person.createdDate, person.updatedDate,
                        icon.id, icon.path))
                .from(person)
                .leftJoin(person.icon, icon)

                .leftJoin(follow)
                .on(person.id.eq(follow.person.id))
                .on(follow.user.id.eq(userId))
                .where(person.id.eq(personId))
                .fetchOne();
    }

    @Override
    public List<RemarkForPersonDetailQueryDto> getRemarkListForPersonDetail(RemarkForPersonDetailSearchCondition condition, Long id) {

        List<RemarkForPersonDetailQueryDto> results = queryFactory
                .select(new QRemarkForPersonDetailQueryDto(
                        remark.id, remark.content, remark.remarkDate,
                        remark.createdDate, remark.updatedDate, remark.likeCount,
                        remark.dislikeCount, remark.sourceSort, remark.sourceUrl,
                        user.id, user.nickname))
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

        return tagList.stream().collect(Collectors.groupingBy(RemarkTagQueryDto::getRemarkId));
    }

    private List<Long> toRemarkIdList(List<RemarkForPersonDetailQueryDto> results) {
        return results.stream()
                .map(RemarkForPersonDetailQueryDto::getRemarkId)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, PersonIndexDto> getPersonCount() {
        List<PersonIndexDto> result = queryFactory
                .select(new QPersonIndexDto(
                        person.category, person.category.count().intValue(), person.remarkList.size().sum()
                ))
                .from(person)
                .groupBy(person.category)
                .where(person.remarkList.size().goe(1))
                .fetch();

        return result.stream().collect(Collectors.toMap(PersonIndexDto::getCategory, Function.identity()));
    }

    @Override
    public List<String> getFirstLettersFromPersonNames(String category) {

        return queryFactory
                .select(person.name.substring(0, 1))
                .from(person)
                .groupBy(person.name.substring(0, 1))
                .where(person.category.eq(category)
                        .and(person.remarkList.size().goe(1)))
                .fetch();
    }

    @Override
    public List<PersonListResponseDto> getPersonInfoForPersonList(String category, String index){

        return queryFactory
                .select(new QPersonListResponseDto(
                        person.id, person.name, person.remarkList.size(), person.job
                ))
                .from(person)
                .where(person.category.eq(category)
                        .and(person.remarkList.size().goe(1))
                        .and(indexRegexp(index)))
                .orderBy(person.name.asc())
                .fetch();
    }

    private BooleanExpression indexRegexp(String index) {

        String pattern = "^[a-zA-Z0-9]"; //영문 또는 숫자
        boolean isNotKorean = Pattern.matches(pattern, index.substring(0,1));

        System.out.println("index : " + index.substring(0,1) + ", isNotKorean : " + isNotKorean);

        if(!isNotKorean) {

            List<String> index_list = new ArrayList<>();
            Map<Integer, String> index_map = new HashMap<>();
            int num = 0;

            index_list.add("ㄱ"); index_list.add("ㄴ"); index_list.add("ㄷ");
            index_list.add("ㄹ"); index_list.add("ㅁ"); index_list.add("ㅂ");
            index_list.add("ㅅ"); index_list.add("ㅇ"); index_list.add("ㅈ");
            index_list.add("ㅊ"); index_list.add("ㅋ"); index_list.add("ㅌ");
            index_list.add("ㅍ"); index_list.add("ㅎ");

            index_map.put(0, "가");  index_map.put(1, "나");  index_map.put(2, "다");
            index_map.put(3, "라");  index_map.put(4, "마");  index_map.put(5, "바");
            index_map.put(6, "사");  index_map.put(7, "아");  index_map.put(8, "자");
            index_map.put(9, "차");  index_map.put(10, "카");  index_map.put(11, "타");
            index_map.put(12, "파");  index_map.put(13, "하"); index_map.put(14, "힣");

            for( int i = 0; i < index_list.size(); i++ ) {
                if( index.equals(index_list.get(i)) ) {
                    num = i; break;
                }
            }

            return person.name.between(index_map.get(num), index_map.get(num+1));

        }else {
            return person.name.notBetween("가", "힣");
        }

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

    private List<OrderSpecifier<?>> getOrderSpecifier(String sort) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (sort != null) {
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

    public StringTemplate replaceCommaOnPersonAlias() {
        return Expressions.stringTemplate("replace({0},',','')", person.alias);
    }

    public StringTemplate replaceSpacePersonName() {
        return Expressions.stringTemplate("replace({0},' ','')", person.name);
    }


}
