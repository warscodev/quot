package com.udpr.quot.domain.person.icon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.web.dto.person.Icon.IconQueryDto;
import com.udpr.quot.web.dto.person.Icon.PersonSummaryForIconDto;
import com.udpr.quot.web.dto.person.Icon.QIconQueryDto;
import com.udpr.quot.web.dto.person.Icon.QPersonSummaryForIconDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.udpr.quot.domain.person.QPerson.person;
import static com.udpr.quot.domain.person.icon.QIcon.icon;

@RequiredArgsConstructor
@Repository
public class IconApiQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<IconQueryDto> getIconList(){

        List<IconQueryDto> results = queryFactory
                .select(new QIconQueryDto(
                        icon.id, icon.path, icon.category, icon.createdDate, icon.updatedDate
                ))
                .from(icon)
                .orderBy(icon.createdDate.desc())
                .fetch();

            List<Long> iconIdList = results.stream()
                    .map(IconQueryDto::getId)
                    .collect(Collectors.toList());

            List<PersonSummaryForIconDto> iconPersonList = queryFactory
                    .select(new QPersonSummaryForIconDto(
                            person.id, person.name, person.job, icon.id
                    ))
                    .from(person)
                    .leftJoin(person.icon, icon)
                    .where(icon.id.in(iconIdList))
                    .fetch();

        Map<Long, List<PersonSummaryForIconDto>> personSummaryMap = iconPersonList.stream()
                .collect(Collectors.groupingBy(PersonSummaryForIconDto::getIconId));

        results.forEach(r->{
            r.setPersonList(personSummaryMap.get(r.getId()));
        });

        return results;
    }

    public IconQueryDto getIcon(Long iconId){
        IconQueryDto result = queryFactory.select(new QIconQueryDto(
                        icon.id, icon.path, icon.category, icon.createdDate, icon.updatedDate
                )).from(icon)
                .where(icon.id.eq(iconId))
                .fetchOne();

        List<PersonSummaryForIconDto> iconPersonList = queryFactory
                .select(new QPersonSummaryForIconDto(
                        person.id, person.name, person.job, icon.id
                ))
                .from(person)
                .leftJoin(person.icon, icon)
                .where(icon.id.eq(iconId))
                .fetch();

            Objects.requireNonNull(result).setPersonList(iconPersonList);

        return result;
    }

    public void deleteIconPresetAndFromPerson(Long iconId){
        queryFactory.update(person)
                .setNull(person.icon.id)
                .where(person.icon.id.eq(iconId))
                .execute();
    }

    public void deleteIconPresetFromPerson(Long personId, Long iconId){
        queryFactory.update(person)
                .setNull(person.icon.id)
                .where(person.icon.id.eq(iconId)
                        .and(person.id.eq(personId)))
                .execute();
    }

}
