package com.udpr.quot.web.dto.tag;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.domain.tag.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDto {

    Long id;
    String name;

    @QueryProjection
    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDto(Tag entity){
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
