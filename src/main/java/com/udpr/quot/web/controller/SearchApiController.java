package com.udpr.quot.web.controller;

import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.service.remark.RemarkService;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.remark.RemarkListResponseDto;
import com.udpr.quot.web.dto.person.PersonAutoCompleteDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final RemarkService remarkService;
    private final PersonService personService;

    /*@GetMapping("/api/search/remark")
    public RemarkListResponseDto getRemarkList(Pageable pageable, RemarkSearchCondition condition) {
        return remarkService.searchRemark(condition, pageable);
    }*/

    @GetMapping("/api/search/personList")
    public List<SearchPersonResponseDto> getPersonList(String keyword){
        return personService.searchPerson(keyword);
    }


    @GetMapping("/api/autoComplete")
    public List<PersonAutoCompleteDto> getPersonListForAutoComplete(String term){
        return personService.personAutoComplete(term);
    }




}
