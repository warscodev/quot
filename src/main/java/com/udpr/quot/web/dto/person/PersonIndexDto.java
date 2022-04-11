package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PersonIndexDto {

    String category;
    int personCount;
    int remarkCount;


    @QueryProjection
    public PersonIndexDto(String category, int personCount, int remarkCount) {
        this.category = category;
        this.personCount = personCount;
        this.remarkCount = remarkCount;
    }
}
