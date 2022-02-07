package com.udpr.quot.web.dto.person.Icon;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class IconQueryDto {

    private final Long id;
    private final String path;
    private final String category;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;

    private List<PersonSummaryForIconDto> personList;

    @QueryProjection
    public IconQueryDto(Long iconId, String path, String category, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = iconId;
        this.path = path;
        this.category = category;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void setPersonList(List<PersonSummaryForIconDto> personList){
        this.personList = personList;
    }
}
