package com.udpr.quot.web.dto.person;

import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.web.dto.person.utils.PersonDtoUtils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PersonResponseDto {

    private Long id;
    private String name;
    private String alias;
    private Birth birth;
    private String gender;
    private String job;
    private String organization;
    private String summary;
    private Status status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String category;

    public PersonResponseDto(Person entity) {
        this.id = entity.getId();;
        this.name = entity.getName();
        this.alias = entity.getAlias();
        this.birth = entity.getBirth();
        this.gender = entity.getGender();
        this.job = entity.getJob();
        this.summary = entity.getSummary();
        this.status = entity.getStatus();
        this.category = entity.getCategory();
        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
    }


}
