package com.udpr.quot.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.udpr.quot.service.comment.CommentService;
import com.udpr.quot.web.dto.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class CommentApiController {

    private final CommentService commentService;


    //코멘트 저장
    @PostMapping("/api/person/{personId}/comment")
    public Long save(@PathVariable("personId") Long personId, @RequestBody CommentRequestDto requestDto)
            throws JsonProcessingException {

        //json형태의 태그값들을 변환하여 dto에 set타입으로 저장하는 메서드
        requestDto.jsonArrayToSet();

        return commentService.save(requestDto, personId);
    }

    //삭제
    @DeleteMapping("/api/comment/{commentId}")
    public Long delete(@PathVariable Long commentId){

        commentService.delete(commentId);

        return commentId;
    }

    //수정
    @PutMapping("/api/comment/{commentId}")
    public Long update(@PathVariable("commentId") Long commentId,
                       @RequestBody CommentRequestDto requestDto) throws JsonProcessingException{
        //json형태의 태그값들을 변환하여 dto에 set타입으로 저장하는 메서드
        requestDto.jsonArrayToSet();

        return commentService.update(requestDto, commentId);
    }

}
