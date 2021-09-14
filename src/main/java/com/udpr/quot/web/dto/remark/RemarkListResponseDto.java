package com.udpr.quot.web.dto.remark;

import com.udpr.quot.web.dto.remark.query.RemarkQueryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RemarkListResponseDto {

    private List<RemarkQueryDto> remarkList;

    private PageMetadata pageMetadata;
}
