package com.udpr.quot.web.dto.remark.comment;

import com.udpr.quot.web.dto.remark.PageMetadata;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CommentListResponseDto {

    private Page<CommentQueryDto> comments;

    private PageMetadata pageMetadata;
}
