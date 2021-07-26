package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.RemarkTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RemarkTagRepository extends JpaRepository<RemarkTag, Long> {
    @Modifying
    @Query("DELETE FROM RemarkTag c WHERE c.remark.id =:remarkId")
    void deleteByRemarkId(@Param("remarkId") Long remarkId);
}
