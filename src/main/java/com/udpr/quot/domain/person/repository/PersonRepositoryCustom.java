package com.udpr.quot.domain.person.repository;

import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.web.dto.person.*;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PersonRepositoryCustom{

    List<AdminPersonListResponseDto> search(PersonSearchCondition condition);

    Page<SearchPersonResponseDto> findByPersonName(String keyword, Pageable pageable);

    /*Page<SearchPersonResponseDto> findByPersonNameApiService(String Keyword, Pageable page);*/

    List<PersonAutoCompleteDto> personAutoComplete(String keyword);

    List<PersonAutoCompleteDto> personAutoCompleteForMainSearch(String keyword);

    List<RemarkForPersonDetailQueryDto> getRemarkListForPersonDetail(RemarkForPersonDetailSearchCondition condition, Long id);

    PersonQueryDto getDetail(Long id);

    PersonQueryDto getDetail(Long personId, Long userId);

    Map<String, PersonIndexDto> getPersonCount();

    List<String> getFirstLettersFromPersonNames(String category);

    List<PersonListResponseDto> getPersonInfoForPersonList(String category, String index);
}
