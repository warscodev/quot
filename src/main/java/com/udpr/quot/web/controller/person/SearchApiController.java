package com.udpr.quot.web.controller.person;

import com.udpr.quot.domain.user.repository.FollowQueryRepository;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.service.remark.RemarkService;
import com.udpr.quot.web.dto.person.PersonAutoCompleteDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final PersonService personService;
    private final FollowQueryRepository followQueryRepository;


    @GetMapping("/api/search/personList")
    public Page<SearchPersonResponseDto> getPersonList(String keyword, Pageable pageable){
            return personService.searchPerson(keyword, pageable);
    }

    @GetMapping("/api/search/followerList")
    public Page<SearchPersonResponseDto> getFollowerList(Long userId, Pageable pageable){
            return followQueryRepository.findFollowerList(userId, pageable);
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
