package com.udpr.quot.web.dto.remark.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RemarkCommentQueryDto {

    private final Long remarkId;
    private final Long commentCount;

    @QueryProjection
    public RemarkCommentQueryDto(Long remarkId, Long commentCount) {
        this.remarkId = remarkId;
        this.commentCount = commentCount;
    }
}
