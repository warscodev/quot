package com.udpr.quot.web.dto.bookmark;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BookmarkQueryDto {

    private final Long  remarkId;
    private final Long isBookmarked;

    @QueryProjection
    public BookmarkQueryDto(Long remarkId, Long isBookmarked) {
        this.remarkId = remarkId;
        this.isBookmarked = isBookmarked;
    }

}
