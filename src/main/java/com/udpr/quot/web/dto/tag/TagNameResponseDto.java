package com.udpr.quot.web.dto.tag;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TagNameResponseDto {
    String name;


    @QueryProjection
    public TagNameResponseDto(String name){
        this.name = name;
    }

}


