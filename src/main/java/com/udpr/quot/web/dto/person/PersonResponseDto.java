package com.udpr.quot.web.dto.person;

import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.icon.Icon;
import com.udpr.quot.web.dto.person.utils.PersonDtoUtils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PersonResponseDto {

    private final Long id;
    private final String name;
    private final String alias;
    private final Birth birth;
    private final String gender;
    private final String job;
    private final String organization;
    private final String summary;
    private final Status status;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;
    private final String category;
    private final String image;
    private Long iconId;
    private String iconPath;

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
        this.organization = entity.getOrganization();
        this.image = entity.getImage();
        if(entity.getIcon() != null){
            this.iconId = entity.getIcon().getId();
            this.iconPath = entity.getIcon().getPath();
        }
    }


}
