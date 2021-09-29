package com.udpr.quot.domain.person.repository;

import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.web.dto.person.PersonAutoCompleteDto;
import com.udpr.quot.web.dto.person.PersonListResponseDto;
import com.udpr.quot.web.dto.person.PersonQueryDto;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;

import java.util.List;

public interface PersonRepositoryCustom{

    List<PersonListResponseDto> search(PersonSearchCondition condition);

    List<SearchPersonResponseDto> findByPersonName(String keyword);

    List<PersonAutoCompleteDto> personAutoComplete(String keyword);

    List<PersonAutoCompleteDto> personAutoCompleteForMainSearch(String keyword);

    List<RemarkForPersonDetailQueryDto> getRemarkListForPersonDetail(RemarkForPersonDetailSearchCondition condition, Long id);

    PersonQueryDto getDetail(Long id);

}
