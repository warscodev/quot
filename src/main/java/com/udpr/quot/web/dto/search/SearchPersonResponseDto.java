package com.udpr.quot.web.dto.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SearchPersonResponseDto {

    Long id;
    String name;
    String job;
    String category;

    @QueryProjection
    public SearchPersonResponseDto(Long id, String name, String job, String category) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.category = category;
    }
}
