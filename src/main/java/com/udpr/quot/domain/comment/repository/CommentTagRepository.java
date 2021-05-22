package com.udpr.quot.domain.comment.repository;

import com.udpr.quot.domain.comment.CommentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
    @Modifying
    @Query("DELETE FROM CommentTag c WHERE c.comment.id =:commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);
}
