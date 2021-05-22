package com.udpr.quot.domain.comment.repository;

import com.udpr.quot.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{


}
