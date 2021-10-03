package com.udpr.quot.web.controller;

import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.service.remark.RemarkService;
import com.udpr.quot.web.dto.person.PersonAutoCompleteDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<SearchPersonResponseDto> getPersonList(String keyword, Pageable pageable){
        return personService.searchPerson(keyword, pageable);
    }

    @GetMapping("/api/autoComplete")
    public List<PersonAutoCompleteDto> getPersonListForAutoComplete(String term){
        return personService.personAutoComplete(term);
    }

    @GetMapping("/api/autoCompleteForMainSearch")
    public List<PersonAutoCompleteDto> getPersonListForAutoCompleteForMainSearch(String term){
        return personService.personAutoCompleteForMainSearch(term);
    }




}
