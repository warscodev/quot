package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PersonListResponseDto {

    private final Long id;
    private final String name;
    private final int count;
    private final String job;


    @QueryProjection
    public PersonListResponseDto(Long id, String name, int count, String job) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.job = job;
    }
}
