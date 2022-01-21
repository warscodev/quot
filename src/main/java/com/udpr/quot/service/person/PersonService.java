package com.udpr.quot.service.person;

import com.udpr.quot.config.S3Uploader;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.domain.remark.repository.RemarkPersonPageQueryRepository;
import com.udpr.quot.domain.user.Follow;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.domain.user.repository.FollowRepository;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.person.*;
import com.udpr.quot.web.dto.remark.RemarkForPersonDetailQueryDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final RemarkPersonPageQueryRepository personPageQueryRepository;
    private final S3Uploader s3Uploader;
    private static final String dirName = "person";

    //저장
    @Transactional
    public Long save(PersonRequestDto requestDto) {
        return personRepository.save(requestDto.toEntity()).getId();
    }

    //수정
    @Transactional
    public Long update(PersonRequestDto dto) throws IOException {
        Long id = dto.getId();

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + id));

        if(!dto.getMultipartFile().isEmpty()){
            String imageUrl = s3Uploader.upload(dto.getMultipartFile(),dirName);
            if(person.getImage() != null && !person.getImage().isEmpty() && !imageUrl.equals(person.getImage())){
                s3Uploader.delete(person.getImage());
            }
            dto.setImage(imageUrl);
        }else{
            if(!dto.getImage().equals(person.getImage()) && person.getImage() != null && !person.getImage().isEmpty()){
                s3Uploader.delete(person.getImage());
            }
        }

        dto.setBirthDay();

        person.update(dto.getName(), dto.getAlias(), dto.getBirth(),
                dto.getJob(), dto.getGender(), dto.getSummary(),
                dto.getCategory(), dto.getOrganization(), dto.getImage());

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


    public Page<SearchPersonResponseDto> searchPerson(String keyword, Pageable pageable) {
        return personRepository.findByPersonName(keyword, pageable);
    }

    public List<PersonAutoCompleteDto> personAutoComplete(String term) {
        return personRepository.personAutoComplete(term);
    }

    public List<PersonAutoCompleteDto> personAutoCompleteForMainSearch(String term) {
        return personRepository.personAutoCompleteForMainSearch(term);
    }

    public PersonDetailDto getDetail(RemarkForPersonDetailSearchCondition condition, Long id){
        PersonQueryDto getDetail = personRepository.getDetail(id);
        List<Integer> yearList = personPageQueryRepository.getYears(id);
        if(yearList.size() ==0){
            return new PersonDetailDto(getDetail);
        }else {
            if(condition.getYear() == null){
                condition.setYear(yearList.get(0));
            }
            List<RemarkForPersonDetailQueryDto> remarkList = personPageQueryRepository.getRemarkListForPersonDetail(condition, id);
            return new PersonDetailDto(getDetail, remarkList, yearList, condition.getYear());
        }
    }

    public PersonDetailDto getDetail(RemarkForPersonDetailSearchCondition condition, Long personId, Long userId){
        PersonQueryDto getDetail = personRepository.getDetail(personId, userId);
        List<Integer> yearList = personPageQueryRepository.getYears(personId);
        if(yearList.size() ==0){
            return new PersonDetailDto(getDetail);
        }else {
            if(condition.getYear() == null){
                condition.setYear(yearList.get(0));
            }
            List<RemarkForPersonDetailQueryDto> remarkList = personPageQueryRepository.getRemarkListForPersonDetail(condition, personId);
            return new PersonDetailDto(getDetail, remarkList, yearList, condition.getYear());
        }
    }


    @Transactional
    public String saveOrDeleteFollow(Long personId, Long userId){

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물 정보가 없습니다. id = " + personId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 정보가 없습니다. id = " + userId));

        Follow follow = followRepository.findByPersonIdAndUserId(personId, userId)
                .orElse(Follow.builder()
                        .person(person)
                        .user(user)
                        .build());

        if(follow.getId() != null){
            followRepository.delete(follow);
            return "unfollowed";
        }else {
            followRepository.save(follow);
            return "followed";
        }
    }
}


