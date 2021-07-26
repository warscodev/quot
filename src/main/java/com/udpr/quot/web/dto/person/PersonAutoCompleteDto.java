package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PersonAutoCompleteDto {

    private Long id;

    private String name;

    private String job;



    @QueryProjection
    public PersonAutoCompleteDto(Long id, String name, String job) {
        this.id = id;
        this.name = name;
        this.job = job;
    }


}
