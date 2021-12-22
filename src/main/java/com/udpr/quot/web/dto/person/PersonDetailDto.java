package com.udpr.quot.web.dto.person;

import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class PersonDetailDto {

    PersonQueryDto person;
    List<RemarkForPersonDetailQueryDto> remarkList;
    List<Integer> yearList;
    int currentYear;
    int prevYear;
    int nextYear;

    public PersonDetailDto(PersonQueryDto person, List<RemarkForPersonDetailQueryDto> remarkList, List<Integer> yearList, int year) {
        this.person = person;
        this.remarkList = remarkList;
        this.yearList = yearList;
        this.currentYear = year;
        this.prevYear = setPrevYear(yearList, year);
        this.nextYear = setNextYear(yearList, year);
    }

    public PersonDetailDto(PersonQueryDto person) {
        this.person = person;
    }

    public int setNextYear(List<Integer> yearList, int currentYear) {
        int currentIndex = yearList.indexOf(currentYear);
        if (yearList.size() > 0 && currentIndex + 1 < yearList.size() && yearList.get(currentIndex + 1) != null) {
            return yearList.get(currentIndex + 1);
        } else {
            return 0;
        }
    }

    public int setPrevYear(List<Integer> yearList, int currentYear) {
        int currentIndex = yearList.indexOf(currentYear);
        if (yearList.size() > 0 && currentIndex > 0 && yearList.get(currentIndex - 1) != null) {
            return yearList.get(currentIndex - 1);
        } else {
            return 0;
        }
    }


}
