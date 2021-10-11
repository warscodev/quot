package com.udpr.quot.web.controller;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.remark.comment.repository.CommentRepository;
import com.udpr.quot.service.remark.comment.CommentService;
import com.udpr.quot.web.dto.remark.comment.CommentQueryDto;
import com.udpr.quot.web.dto.remark.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @PostMapping("/api/remark/{remarkId}/comment")
    public Long saveComment(@PathVariable("remarkId") Long remarkId, @RequestBody @Valid CommentRequestDto dto,
                            @LoginUser SessionUser user) throws AuthenticationException {
        if (user != null) {
            return commentService.saveComment(remarkId, user.getId(), dto);
        } else
            throw new AuthenticationException();
    }

    @GetMapping("/api/remark/{remarkId}/comment")
    public Page<CommentQueryDto> getComments(@PathVariable("remarkId") Long remarkId, Pageable pageable) {
        return commentRepository.getComments(remarkId, pageable);
    }

    @DeleteMapping("/api/remark/{remarkId}/comment/{commentId}/{userId}")
    public Long deleteComment(@PathVariable("remarkId") Long remarkId, @PathVariable("commentId") Long commentId,
                              @LoginUser SessionUser user, @PathVariable("userId") Long userId) throws AuthenticationException {
        if (user != null && userId.equals(user.getId())) {
            commentService.deleteComment(commentId);
            return commentRepository.getCommentCount(remarkId);
        } else
            throw new AuthenticationException();
    }

    @PutMapping("/api/remark/{remarkId}/comment/{commentId}/{userId}")
    public Long modifyComment(@PathVariable("remarkId") Long remarkId, @PathVariable("commentId") Long commentId,
                              @LoginUser SessionUser user, @PathVariable("userId") Long userId, @RequestBody @Valid CommentRequestDto dto) throws AuthenticationException {

        if (user != null && userId.equals(user.getId())) {
            commentService.modifyComment(commentId,dto.getContent());
            return commentRepository.getCommentCount(remarkId);
        } else
            throw new AuthenticationException();

    }
}
