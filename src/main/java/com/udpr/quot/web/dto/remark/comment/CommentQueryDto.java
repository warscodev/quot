package com.udpr.quot.web.dto.remark.comment;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.domain.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class CommentQueryDto {

    private Long commentId;

    private String content;

    private String createdDate;

    private String updatedDate;

    private Status status;

    private Long ancestorId;

    private Long parentId;

    private String parentWriter;

    private Long userId;

    private String nickname;

    @QueryProjection
    public CommentQueryDto(Long commentId, String content, LocalDateTime createdDate, LocalDateTime updatedDate, Status status, Long ancestorId, Long parentId, String parentWriter, Long userId, String nickname) {
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdAndUpdatedDateFormat(createdDate);
        this.updatedDate = createdAndUpdatedDateFormat(updatedDate);
        this.status = status;
        this.ancestorId = ancestorId;
        this.parentId = parentId;
        this.parentWriter = parentWriter;
        this.userId = userId;
        this.nickname = nickname;
    }

    public String createdAndUpdatedDateFormat(LocalDateTime commentDate){
        LocalDateTime now = LocalDateTime.now();
        long betweenMinutes = ChronoUnit.MINUTES.between(commentDate, now);
        if(betweenMinutes >= 60*24){
            return commentDate.format(DateTimeFormatter.ofPattern("yy. MM. dd."));
        }else if(betweenMinutes >= 60){
            long betweenHour = ChronoUnit.HOURS.between(commentDate, now);
                return betweenHour +"시간 전";
        }else if(betweenMinutes >= 1){
            return betweenMinutes +"분 전";
        }else{
            return "방금 전";
        }
    }

}
