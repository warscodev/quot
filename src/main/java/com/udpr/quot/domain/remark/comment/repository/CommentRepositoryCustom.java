package com.udpr.quot.domain.remark.comment.repository;

import com.udpr.quot.web.dto.remark.comment.CommentQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom{

    Page<CommentQueryDto> getComments(Long remarkId, Pageable pageable);

    Long getCommentCount(Long remarkID);

    Long checkChildren(Long ancestorId);

    void checkAncestorStatus(Long commentId);

}
