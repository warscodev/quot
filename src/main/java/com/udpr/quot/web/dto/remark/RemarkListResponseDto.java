package com.udpr.quot.web.dto.remark;

import com.udpr.quot.web.dto.remark.query.RemarkQueryDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RemarkListResponseDto {

    private List<SearchPersonResponseDto> personList;

    private List<RemarkQueryDto> remarkList;

    private PageMetadata pageMetadata;

    public RemarkListResponseDto(List<RemarkQueryDto> remarkList, PageMetadata pageMetadata) {
        this.remarkList = remarkList;
        this.pageMetadata = pageMetadata;
    }
}
