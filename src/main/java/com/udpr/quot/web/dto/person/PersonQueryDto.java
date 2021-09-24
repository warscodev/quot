package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonQueryDto {

    private Long personId;
    private String name;
    private String alias;
    private Birth birth;
    private String gender;
    private String job;
    private String summary;
    private String category;

    private List<RemarkForPersonDetailQueryDto> remarkList;

    @QueryProjection
    public PersonQueryDto(Long personId, String name, String alias, Birth birth, String gender, String job, String summary, String category) {
        this.personId = personId;
        this.name = name;
        this.alias = alias;
        this.birth = birth;
        this.gender = gender;
        this.job = job;
        this.summary = summary;
        this.category = category;
    }
}
