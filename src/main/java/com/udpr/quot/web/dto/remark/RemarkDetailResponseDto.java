package com.udpr.quot.web.dto.remark;

import com.udpr.quot.domain.common.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RemarkDetailResponseDto {

    private Long remarkId;
    private String content;
    private LocalDate remarkDate;
    private String createdDate;
    private String updatedDate;
    private int isLike;
    private int likeCount;
    private int dislikeCount;

}
