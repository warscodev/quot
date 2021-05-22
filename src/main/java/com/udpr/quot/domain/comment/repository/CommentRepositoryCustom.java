package com.udpr.quot.domain.comment.repository;

import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    Page<Comment> searchAll(Pageable pageable);

    List<TagDto> getTags(Long commentId);

    Page<Comment> searchByPersonName(String searchKeyword, Long personId, Pageable pageable);

    Page<Comment> searchByTagName(String searchKeyword, Pageable pageable);

    Page<Comment> searchByContent(String searchKeyword, Pageable pageable);
}