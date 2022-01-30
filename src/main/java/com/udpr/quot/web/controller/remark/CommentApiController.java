package com.udpr.quot.web.controller.remark;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.remark.comment.repository.CommentRepository;
import com.udpr.quot.domain.reporting.repository.ReportingRepository;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.service.remark.comment.CommentService;
import com.udpr.quot.service.reporting.ReportingService;
import com.udpr.quot.web.dto.remark.PageMetadata;
import com.udpr.quot.web.dto.remark.comment.CommentListResponseDto;
import com.udpr.quot.web.dto.remark.comment.CommentQueryDto;
import com.udpr.quot.web.dto.remark.comment.CommentRequestDto;
import com.udpr.quot.web.dto.reporting.ReportingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReportingRepository reportingRepository;
    private final ReportingService reportingService;

    @PostMapping("/api/remark/{remarkId}/comment")
    public Long saveComment(@PathVariable("remarkId") Long remarkId, @RequestBody @Valid CommentRequestDto dto,
                            @LoginUser SessionUser user) throws AuthenticationException {
        if (user != null) {
            return commentService.saveComment(remarkId, user.getId(), dto);
        } else
            throw new AuthenticationException();
    }

    @GetMapping("/api/remark/{remarkId}/comment")
    public CommentListResponseDto getComments(@PathVariable("remarkId") Long remarkId, Pageable pageable) {

        System.out.println("호출!");

        Pageable commentPageable = PageRequest.of(pageable.getPageNumber()-1,40);

        Page<CommentQueryDto> comments = commentRepository.getComments(remarkId, commentPageable);
        PageMetadata pageMetadata = new PageMetadata(comments);

        return new CommentListResponseDto(comments, pageMetadata);
    }

    @DeleteMapping("/api/remark/{remarkId}/comment/{commentId}/{userId}")
    public Long deleteComment(@PathVariable("remarkId") Long remarkId, @PathVariable("commentId") Long commentId,
                              @LoginUser SessionUser user, @PathVariable("userId") Long userId, @RequestParam(value = "ancestorId", required = false) Long ancestorId) throws AuthenticationException {
        if (user != null && userId.equals(user.getId())) {
            if(ancestorId != null){
                commentService.deleteComment(commentId,ancestorId);
            }else {
                commentService.deleteComment(commentId);
            }
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

    @PostMapping("/api/comment/{commentId}/reporting")
    public String reportingComment(@PathVariable("commentId") Long commentId,
                                 @RequestBody ReportingRequestDto dto, @LoginUser SessionUser user){

        return reportingService.saveReporting(commentId, user.getId(), dto);
    }
}
