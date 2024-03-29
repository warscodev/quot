package com.udpr.quot.domain.remark.comment.repository;

import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.remark.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Comment findByIdAndStatusNot(Long ancestorId, Status status);

}
