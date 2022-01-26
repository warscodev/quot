package com.udpr.quot.web.dto.remark.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class RemarkQueryDto {

    private Long remarkId;
    private String content;
    private String remarkDate;
    private String createdDate;
    private String updatedDate;
    private int likeCount;
    private int dislikeCount;
    private String sourceSort;
    private String sourceUrl;

    private List<RemarkTagQueryDto> remarkTagList;

    private List<RemarkLikeQueryDto> remarkLikeList;

    private String tags;

    private int isLike;

    private Long personId;
    private String name;
    private String alias;
    private String job;
    private String category;
    private String catClassName;
    private String image;

    private Long user_id;
    private String nickname;

    private Long commentCount;

    private Long isBookmarked;

    private String remarkSummary;

    private String remarkSummaryForTitle;
    private String remarkDateForDescription;


    @QueryProjection
    public RemarkQueryDto(Long remarkId, String content, LocalDate remarkDate, LocalDateTime createdDate, LocalDateTime updatedDate,
                          int likeCount, int dislikeCount, String sourceSort, String sourceUrl,
                          Long personId, String name, String alias, String job, String category, String image,
                          Long user_id, String nickname) {
        this.remarkId = remarkId;
        this.content = content;
        this.remarkDate = remarkDateFormat(remarkDate);
        this.createdDate = createdAndUpdatedDateFormat(createdDate);
        this.updatedDate = createdAndUpdatedDateFormat(updatedDate);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
        this.personId = personId;
        this.name = name;
        this.alias = alias;
        this.job = job;
        this.category = category;
        this.image = image;
        this.catClassName = setCatClassName(category);
        this.user_id = user_id;
        this.nickname = nickname;
        this.remarkSummary = summarySubstring(content);
        this.remarkSummaryForTitle = summaryForTitleSubstring(content);
        this.remarkDateForDescription = remarkDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

    public String createdAndUpdatedDateFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
    }

    public String remarkDateFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."));
    }

    public String summarySubstring(String remarkSummary){
        if(remarkSummary.length()>320){
            return remarkSummary.substring(0,317) + "...";
        }else{
            return remarkSummary;
        }
    }
    public String summaryForTitleSubstring(String remarkSummary){
        if(remarkSummary.length()>32){
            return remarkSummary.substring(0,29) + "...";
        }else{
            return remarkSummary;
        }
    }

    public String setCatClassName(String category){

        String className = "";

        switch (category){
            case "정치": className = "politics"; break;
            case "방송연예": className = "broadcast-enter"; break;
            case "사회문화" : className = "society-culture"; break;
            case "스포츠" : className = "sports"; break;
            case "창작물" : className = "creations"; break;
            default: className = "etc"; break;
        }

        return className;
    }
}
