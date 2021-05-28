package com.udpr.quot.service.person;

import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.web.dto.person.PersonListResponseDto;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import com.udpr.quot.web.dto.person.PersonRequestDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PersonService {

    private final PersonRepository personRepository;

    //저장
    @Transactional
    public Long save(PersonRequestDto requestDto) {
        return personRepository.save(requestDto.toEntity()).getId();
    }


    //수정
    @Transactional
    public Long update(Long id, PersonRequestDto requestDto) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + id));

        person.update(requestDto.getName(), requestDto.getAlias(), requestDto.getBirth(),
                requestDto.getJob(), requestDto.getGender(), requestDto.getSummary(), requestDto.getCategory());

        return id;
    }


    //삭제
    @Transactional
    public void delete(Long id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id=" + id));

        personRepository.delete(person);
    }


    //인물 목록
    public List<PersonListResponseDto> search(PersonSearchCondition condition) {
        return personRepository.search(condition);
    }

    //인물 정보
    public PersonResponseDto findById(Long id) {

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + id));

        return new PersonResponseDto(entity);
    }


    public Person findPerson(Long personId) {
        return personRepository.getOne(personId);
    }


    public List<SearchPersonResponseDto> searchPerson(String keyword) {
        return personRepository.findByPersonName(keyword);
    }


}


