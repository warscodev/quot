package com.udpr.quot.service.remark;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.repository.RemarkRepository;
import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.web.dto.remark.RemarkListResponseDto;
import com.udpr.quot.web.dto.remark.RemarkRequestDto;
import com.udpr.quot.web.dto.remark.RemarkResponseDto;
import com.udpr.quot.web.dto.remark.PageMetadata;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class RemarkService {

    private final PersonRepository personRepository;
    private final RemarkRepository remarkRepository;
    private final RemarkTagService remarkTagService;

    //코멘트 저장
    @Transactional
    public Long save(RemarkRequestDto requestDto, Long personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + personId));

        //인물 정보 저장
        requestDto.setPerson(person);

        //코멘트 저장
        Remark savedRemark = remarkRepository.save(requestDto.toEntity());

        //태그 저장
        remarkTagService.saveTags(savedRemark, requestDto.getTags());


        return savedRemark.getId();
    }


    //코멘트 수정
    @Transactional
    public Long update(RemarkRequestDto requestDto, Long remarkId) {

        Remark remark = remarkRepository.findById(remarkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언정보가 없습니다. id = " + remarkId));

        //태그가 변경 되었다면 저장
        if (!remark.TagsToStringList().equals(requestDto.getTags()))
            remarkTagService.saveTags(remark, requestDto.getTags());

        //코멘트 수정
        remark.update(requestDto.getContent(),
                requestDto.getRemarkDate(),
                requestDto.getSourceSort(),
                requestDto.getSourceUrl());

        return remarkId;
    }


    //코멘트 삭제
    @Transactional
    public void delete(Long id) {

        Remark remark = remarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언 정보가 없습니다. id=" + id));

        remarkRepository.delete(remark);
    }


    //코멘트 목록
    public RemarkListResponseDto searchRemark(RemarkSearchCondition condition, Pageable pageable) {

        String keyword = condition.getKeyword();
        int tab = condition.getTab();
        Long personId = condition.getPersonId();

        List<Remark> content = new ArrayList<>();
        Long total_ = 0L;


        Page<Remark> remarks = new PageImpl<>(content, pageable, total_);

        if (keyword == null || keyword.isBlank()) {
            remarks = remarkRepository.searchAll(pageable);
        } else {
            switch (tab) {
                case 1:
                    remarks = remarkRepository.searchByContentOrPersonName(keyword, pageable);
                    break;
                case 2:
                    remarks = remarkRepository.searchByPersonName(keyword, personId, pageable);
                    break;
                case 3:
                    remarks = remarkRepository.searchByTagName(keyword, pageable);
                    break;
            }
        }


        List<RemarkResponseDto> responseDtoList = remarks.stream()
                .map(remark -> RemarkResponseDto.builder()
                    .remarkId(remark.getId())
                        .content(remark.getContent())
                        .remarkDate(remark.getRemarkDate())
                        .remarkDate_format(remark.getRemarkDate())
                        .status(remark.getStatus())
                        .createdDate(remark.getCreatedDate())
                        .updatedDate(remark.getUpdatedDate())
                        .sourceUrl(remark.getSourceUrl())
                        .sourceSort(remark.getSourceSort())
                        .person(new PersonResponseDto(remark.getPerson()))
                        .tags(remarkRepository.getTags(remark.getId()))
                    .build())
                .collect(Collectors.toList());

        PageMetadata pageMetadata = new PageMetadata(remarks);

        return new RemarkListResponseDto(responseDtoList,pageMetadata);
    }



    public RemarkResponseDto findById(Long id) {

        Remark entity = remarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트 정보가 없습니다. id = " + id));

        return new RemarkResponseDto(entity);


    }

}