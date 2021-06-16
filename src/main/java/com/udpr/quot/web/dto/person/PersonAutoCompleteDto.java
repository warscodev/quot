package com.udpr.quot.web.dto.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PersonAutoCompleteDto {

    private Long id;

    /*@JsonProperty("value")*/
    private String name;

    /*@JsonIgnore*/
    private String job;

    /*private String label = getName() + "(" + getJob() + ")";*/


    @QueryProjection
    public PersonAutoCompleteDto(Long id, String name, String job) {
        this.id = id;
        this.name = name;
        this.job = job;
    }


}
