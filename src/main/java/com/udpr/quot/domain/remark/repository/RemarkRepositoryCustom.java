package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.web.dto.remark.RemarkTestDto;
import com.udpr.quot.web.dto.tag.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RemarkRepositoryCustom {

    Page<Remark> searchAll(Pageable pageable);

    List<TagDto> getTags(Long remarkId);

    Page<Remark> searchByPersonName(String searchKeyword, Long personId, Pageable pageable);

    Page<Remark> searchByTagName(String searchKeyword, Pageable pageable);

    Page<Remark> searchByContentOrPersonName(String searchKeyword, Pageable pageable);


}