package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.domain.common.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PersonListResponseDto {

    private Long id;
    private String name;
    private String alias;
    private Birth birth;
    private String job;
    private Status status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String category;

    @QueryProjection
    public PersonListResponseDto(Long id, String name, String alias, Birth birth, String job, Status status, LocalDateTime createdDate, LocalDateTime updatedDate, String category) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.birth = birth;
        this.job = job;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.category = category;
    }
}
