package com.udpr.quot.web.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResponseDto {

    private List<CommentResponseDto> commentList;

    private PageMetadata pageMetadata;
}
