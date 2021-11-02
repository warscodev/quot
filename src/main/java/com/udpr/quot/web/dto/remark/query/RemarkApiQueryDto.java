package com.udpr.quot.web.dto.remark.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class RemarkApiQueryDto {

    private final Long remarkId;
    private final String content;
    private final String remarkDate;
    private final int likeCount;
    private final int dislikeCount;

    private final String name;
    private final String job;

    private final int commentCount;

    @QueryProjection
    public RemarkApiQueryDto(Long remarkId, String content, LocalDate remarkDate, int likeCount, int dislikeCount, String name, String job, int commentCount) {
        this.remarkId = remarkId;
        this.content = content;
        this.remarkDate = remarkDateFormat(remarkDate);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.name = name;
        this.job = job;
        this.commentCount = commentCount;
    }

    public String remarkDateFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

}
