package com.udpr.quot.web.dto.person;

import com.udpr.quot.domain.person.Birth;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PersonUpdateRequestDto {

    private String name;
    private String alias;
    private Birth birth;
    private String gender;
    private String job;
    private String summary;
    private Long categoryId;

    @Builder
    public PersonUpdateRequestDto(String name, String alias, Birth birth, String gender,
                                  String job, String summary, Long categoryId) {
        this.name = name;
        this.alias = alias;
        this.birth = birth;
        this.gender = gender;
        this.job = job;
        this.summary = summary;
        this.categoryId = categoryId;
    }
}
