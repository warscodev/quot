package com.udpr.quot.web.dto.remark.comment;

import com.udpr.quot.web.dto.remark.PageMetadata;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CommentListResponseDto {

    private final Page<CommentQueryDto> comments;

    private final PageMetadata pageMetadata;

    public CommentListResponseDto(Page<CommentQueryDto> comments, PageMetadata pageMetadata) {
        this.comments = comments;
        this.pageMetadata = pageMetadata;
    }
}
