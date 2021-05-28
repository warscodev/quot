package com.udpr.quot.web.controller;

import com.udpr.quot.domain.comment.search.CommentSearchCondition;
import com.udpr.quot.service.comment.CommentService;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.comment.CommentListResponseDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final CommentService commentService;
    private final PersonService personService;

    @GetMapping("/api/comment")
    public CommentListResponseDto getCommentList(Pageable pageable, CommentSearchCondition condition) {
        return commentService.searchComment(condition, pageable);
    }


    @GetMapping("/api/personList")
    public List<SearchPersonResponseDto> getPersonList(String keyword){
        return personService.searchPerson(keyword);
    }




}
