package com.udpr.quot.web.dto.reporting;

import com.udpr.quot.domain.reporting.Reporting;
import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportingRequestDto {

    private String type;
    private String originContent;
    private String message;
    private Comment comment;
    private User reporter;

    public Reporting toEntity(){
        return Reporting.builder()
                .type(type)
                .comment(comment)
                .reporter(reporter)
                .originContent(originContent)
                .build();
    }
}
