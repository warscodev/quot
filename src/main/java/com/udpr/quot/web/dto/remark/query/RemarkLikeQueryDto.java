package com.udpr.quot.web.dto.remark.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RemarkLikeQueryDto {
    private Long remarkId;
    private int isLike;

    @QueryProjection
    public RemarkLikeQueryDto(Long remarkId, int isLike) {
        this.remarkId = remarkId;
        this.isLike = isLike;
    }
}
