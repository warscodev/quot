package com.udpr.quot.domain.person.repository;

import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.web.dto.person.PersonListResponseDto;

import java.util.List;

public interface PersonRepositoryCustom{

    List<PersonListResponseDto> search(PersonSearchCondition condition);


}
