package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.web.dto.remark.query.RemarkQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RemarkRepositoryCustom {

    Page<RemarkQueryDto> searchAll(RemarkSearchCondition condition, Pageable pageable);

    Page<RemarkQueryDto> searchByPersonName(RemarkSearchCondition condition, Pageable pageable);

    Page<RemarkQueryDto> searchByTagName(RemarkSearchCondition condition, Pageable pageable);

    Page<RemarkQueryDto> searchByContentOrPersonName(RemarkSearchCondition condition, Pageable pageable);

    Page<RemarkQueryDto> getBookmarkList(RemarkSearchCondition condition, Pageable pageable);

    Page<RemarkQueryDto> getFollowerRemarkList(RemarkSearchCondition condition, Pageable pageable);

    RemarkQueryDto getDetail(Long remarkId, Long sessionId);
    RemarkQueryDto getDetail(Long remarkId);
}