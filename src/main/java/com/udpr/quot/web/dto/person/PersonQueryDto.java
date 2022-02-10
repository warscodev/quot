package com.udpr.quot.web.dto.person;

import com.querydsl.core.annotations.QueryProjection;
import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class PersonQueryDto {

    private Long personId;
    private String name;
    private String alias;
    private String aliasReps;
    private Birth birth;
    private String birthday;
    private String gender;
    private String job;
    private String organization;
    private String summary;
    private String category;
    private Long followId;
    private String image;
    private Long iconId;
    private String iconPath;
    private String updatedDate;
    private String createdDate;

    private List<RemarkForPersonDetailQueryDto> remarkList;

    @QueryProjection
    public PersonQueryDto(Long personId, String name, String alias, Birth birth, String gender, String job,
                          String summary, String category, Long followId, String organization, String image,
                          LocalDateTime createdDate, LocalDateTime updatedDate, Long iconId, String iconPath) {
        this.personId = personId;
        this.name = name;
        this.alias = alias;
        if(alias !=null && alias.contains(",")){
            this.aliasReps = substringAlias(alias);
        }
        this.birth = birth;
        this.birthday = setBirthday(birth);
        this.gender = gender;
        this.job = job;
        this.summary = summary;
        this.category = category;
        this.followId = followId;
        this.organization = organization;
        this.image = image;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.updatedDate = updatedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.iconId = iconId;
        this.iconPath = iconPath;
    }

    @QueryProjection
    public PersonQueryDto(Long personId, String name, String alias, Birth birth, String gender,
                          String job, String summary, String category, String organization, String image,
                          LocalDateTime createdDate, LocalDateTime updatedDate, Long iconId, String iconPath) {
        this.personId = personId;
        this.name = name;
        this.alias = alias;
        if(alias !=null && alias.contains(",")){
            this.aliasReps = substringAlias(alias);
        }
        this.birth = birth;
        this.birthday = setBirthday(birth);
        this.gender = gender;
        this.job = job;
        this.summary = summary;
        this.category = category;
        this.organization = organization;
        this.image = image;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.updatedDate = updatedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.iconId = iconId;
        this.iconPath = iconPath;
    }

    public String substringAlias(String alias){
        return alias.substring(0,alias.indexOf(","));
    }

    public String setBirthday(Birth birth){

        StringBuilder birthToString = new StringBuilder("");

        if(!birth.getBirth_year().isEmpty()){
            birthToString.append(birth.getBirth_year()).append("년 ");
        }
        if(!birth.getBirth_month().isEmpty()){
            birthToString.append(birth.getBirth_month()).append("월 ");
        }
        if(!birth.getBirth_day().isEmpty()){
            birthToString.append(birth.getBirth_day()).append("일");
        }

        return birthToString.toString().trim();
    }

}
