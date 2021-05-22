package com.udpr.quot.domain.person.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.web.dto.person.PersonListResponseDto;
import com.udpr.quot.web.dto.person.QPersonListResponseDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.udpr.quot.domain.person.QPerson.person;

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
                .orderBy(person.createdDate.desc())
                .fetch();
    }

    private Predicate nameLike(String name) {
        if (name != null && name.length() > 0)
            return person.name.like("%" + name + "%");
        return null;
    }

    private Predicate jobLike(String job) {
        if (job != null && job.length() > 0)
            return person.name.like("%" + job + "%");
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
}
