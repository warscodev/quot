package com.udpr.quot.web.dto.remark;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RemarkListResponseDto {

    private List<RemarkResponseDto> remarkList;

    private PageMetadata pageMetadata;
}
