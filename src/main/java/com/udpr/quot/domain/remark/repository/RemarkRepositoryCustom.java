package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.web.dto.remark.RemarkTestDto;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RemarkRepositoryCustom {

    Page<Remark> searchAll(RemarkSearchCondition condition, Pageable pageable);

    List<TagDto> getTags(Long remarkId);

    Page<Remark> searchByPersonName(RemarkSearchCondition condition, Pageable pageable);

    Page<Remark> searchByTagName(RemarkSearchCondition condition, Pageable pageable);

    Page<Remark> searchByContentOrPersonName(RemarkSearchCondition condition, Pageable pageable);


}