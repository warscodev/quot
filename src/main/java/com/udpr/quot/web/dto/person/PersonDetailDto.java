package com.udpr.quot.web.dto.person;

import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PersonDetailDto {

    PersonQueryDto person;
    List<RemarkForPersonDetailQueryDto> remarkList;

    public PersonDetailDto(PersonQueryDto person, List<RemarkForPersonDetailQueryDto> remarkList) {
        this.person = person;
        this.remarkList = remarkList;
    }
}
