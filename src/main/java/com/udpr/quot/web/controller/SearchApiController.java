package com.udpr.quot.web.controller;

import com.udpr.quot.domain.comment.search.CommentSearchCondition;
import com.udpr.quot.service.comment.CommentService;
import com.udpr.quot.web.dto.comment.CommentListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final CommentService commentService;

    @GetMapping("/api/comment")
    public CommentListResponseDto getCommentList(Pageable pageable, CommentSearchCondition condition) {
        return commentService.searchComment(condition, pageable);
    }



}
