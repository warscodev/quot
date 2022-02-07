package com.udpr.quot.web.dto.person.Icon;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonSummaryForIconDto {
    private Long personId;
    private String personName;
    private String personJob;
    private Long iconId;

    @QueryProjection
    public PersonSummaryForIconDto(Long personId, String personName, String personJob, Long iconId) {
        this.personId = personId;
        this.personName = personName;
        this.personJob = personJob;
        this.iconId = iconId;
    }
}
