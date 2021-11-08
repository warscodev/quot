package com.udpr.quot.web.dto.person;

import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class PersonDetailDto {

    PersonQueryDto person;
    List<RemarkForPersonDetailQueryDto> remarkList;
    List<Integer> yearList;

    public PersonDetailDto(PersonQueryDto person, List<RemarkForPersonDetailQueryDto> remarkList, List<Integer> yearList) {
        this.person = person;
        this.remarkList = remarkList;
        this.yearList = yearList;
    }

    public PersonDetailDto(PersonQueryDto person) {
        this.person = person;
    }
}
