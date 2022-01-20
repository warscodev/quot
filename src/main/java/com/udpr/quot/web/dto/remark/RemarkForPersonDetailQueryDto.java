package com.udpr.quot.web.dto.remark;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.web.dto.remark.query.RemarkTagQueryDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RemarkForPersonDetailQueryDto {

    private Long remarkId;
    private String content;
    private String remarkDate;
    private String createdDate;
    private String updatedDate;
    private int likeCount;
    private int dislikeCount;
    private String sourceSort;
    private String sourceUrl;

    private Long user_id;
    private String nickname;

    private Long commentCount;

    private List<RemarkTagQueryDto> remarkTagList;

    @QueryProjection
    public RemarkForPersonDetailQueryDto(Long remarkId, String content, LocalDate remarkDate, LocalDateTime createdDate, LocalDateTime updatedDate, int likeCount, int dislikeCount, String sourceSort, String sourceUrl, Long user_id, String nickname) {
        this.remarkId = remarkId;
        this.content = content;
        this.remarkDate = remarkDateFormat(remarkDate);
        this.createdDate = createdAndUpdatedDateFormat(createdDate);
        this.updatedDate = createdAndUpdatedDateFormat(updatedDate);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
        this.user_id = user_id;
        this.nickname = nickname;
    }

    public String createdAndUpdatedDateFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }

    public String remarkDateFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    }
}
