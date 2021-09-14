package com.udpr.quot.web.dto.remark.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RemarkTagQueryDto {

    @JsonIgnore
    private Long remarkId;
    @JsonIgnore
    private Long tagId;
    private String name;

    @QueryProjection
    public RemarkTagQueryDto(Long remarkId,Long tagId, String name) {
        this.remarkId = remarkId;
        this.tagId = tagId;
        this.name = name;
    }
}
