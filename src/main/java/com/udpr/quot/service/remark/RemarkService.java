package com.udpr.quot.service.remark;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkLike;
import com.udpr.quot.domain.remark.repository.RemarkLikeRepository;
import com.udpr.quot.domain.remark.repository.RemarkRepository;
import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.remark.*;
import com.udpr.quot.web.dto.remark.query.RemarkQueryDto;
import com.udpr.quot.web.dto.search.SearchPersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RemarkService {

    private final PersonRepository personRepository;
    private final RemarkRepository remarkRepository;
    private final RemarkLikeRepository remarkLikeRepository;
    private final UserRepository userRepository;

    private final RemarkTagService remarkTagService;

    //코멘트 저장
    @Transactional
    public Long save(RemarkRequestDto requestDto, Long personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + personId));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다. id = " + personId));

        //인물 정보 저장
        requestDto.setPerson(person);

        //유저 정보 저장
        requestDto.setUser(user);

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
    public RemarkListResponseDto searchRemark(RemarkSearchCondition condition) {

        Pageable pageable = PageRequest.of(condition.getPage()-1,10);

        String keyword = condition.getKeyword();
        int tab = condition.getTab();
        List<RemarkQueryDto> content = new ArrayList<>();
        Long total_ = 0L;


        Page<RemarkQueryDto> remarks = new PageImpl<>(content, pageable, total_);
        List<SearchPersonResponseDto> personList = new ArrayList<>();

        if (keyword == null || keyword.isBlank()) {
            remarks = remarkRepository.searchAll(condition, pageable);
        } else {

            personList = personRepository.findByPersonName(condition.getKeyword());

            switch (tab) {
                case 1:
                    remarks = remarkRepository.searchByContentOrPersonName(condition, pageable);
                    break;
                case 2:
                    remarks = remarkRepository.searchByPersonName(condition, pageable);
                    break;
                case 3:
                    remarks = remarkRepository.searchByTagName(condition, pageable);
                    break;
            }
        }

        List<RemarkQueryDto> dtoList = remarks.getContent();

        PageMetadata pageMetadata = new PageMetadata(remarks);

        if(personList.size() !=0){
            return new RemarkListResponseDto(personList, dtoList, pageMetadata);
        }else{
            return new RemarkListResponseDto(dtoList, pageMetadata);

        }
    }

    public RemarkQueryDto getDetail(Long remarkId, Long SessionId) {
        return remarkRepository.getDetail(remarkId, SessionId);
    }

    public RemarkQueryDto getDetail(Long remarkId) {
        return remarkRepository.getDetail(remarkId);
    }

    public RemarkResponseDto findById(Long id) {

        Remark entity = remarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언 정보가 없습니다. id = " + id));

        return new RemarkResponseDto(entity);
    }

    //좋아요
    @Transactional
    public LikeInfo remarkLike(Long remarkId, Long userId, int isLike) {
        Remark remark = remarkRepository.findById(remarkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언 정보가 없습니다. id = " + remarkId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 정보가 없습니다. id = " + userId));

        //좋아요 기록이 없는경우
        if (hasNotLiked(remark, user)) {
            remarkLikeRepository.save(RemarkLike.builder()
                    .remark(remark)
                    .user(user)
                    .isLike(isLike).build());
            remark.increaseLikeCount(isLike);

        //좋아요 기록이 있는 경우
        } else {
            int savedIsLike = remarkLikeRepository.getIsLike(remark, user);

            if (isLike == savedIsLike) {
                remarkLikeRepository.deleteByRemarkAndUser(remark, user);
                remark.decreaseLikeCount(isLike);
            } else {
                remarkLikeRepository.update(remark, user, isLike);
                remark.increaseLikeCount(isLike);
                remark.decreaseLikeCount(savedIsLike);
            }
        }

        return new LikeInfo(remark.getLikeCount(),remark.getDislikeCount(),isLike);

    }

    public Boolean hasNotLiked(Remark remark, User user) {
        return remarkLikeRepository.findByRemarkAndUser(remark, user).isEmpty();
    }


}