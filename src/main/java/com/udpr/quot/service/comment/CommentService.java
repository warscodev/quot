package com.udpr.quot.service.comment;

import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.domain.comment.repository.CommentRepository;
import com.udpr.quot.domain.comment.search.CommentSearchCondition;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.web.dto.comment.CommentListResponseDto;
import com.udpr.quot.web.dto.comment.CommentResponseDto;
import com.udpr.quot.web.dto.comment.CommentRequestDto;
import com.udpr.quot.web.dto.comment.PageMetadata;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final PersonRepository personRepository;
    private final CommentRepository commentRepository;
    private final CommentTagService commentTagService;

    //코멘트 저장
    @Transactional
    public Long save(CommentRequestDto requestDto, Long personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물정보가 없습니다. id = " + personId));

        //인물 정보 저장
        requestDto.setPerson(person);

        //코멘트 저장
        String content = requestDto.getContent();
        Comment savedComment = commentRepository.save(requestDto.toEntity());

        //태그 저장
        commentTagService.saveTags(savedComment, requestDto.getTags());


        return savedComment.getId();
    }


    //코멘트 수정
    @Transactional
    public Long update(CommentRequestDto requestDto, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언정보가 없습니다. id = " + commentId));

        //태그가 변경 되었다면 저장
        if (!comment.TagsToStringList().equals(requestDto.getTags()))
            commentTagService.saveTags(comment, requestDto.getTags());

        //코멘트 수정
        comment.update(requestDto.getContent(),
                requestDto.getCommentDate(),
                requestDto.getSourceSort(),
                requestDto.getSourceUrl());

        return commentId;
    }


    //코멘트 삭제
    @Transactional
    public void delete(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언 정보가 없습니다. id=" + id));

        commentRepository.delete(comment);
    }


    //코멘트 목록
    public CommentListResponseDto searchComment(CommentSearchCondition condition, Pageable pageable) {

        String keyword = condition.getKeyword();
        int tab = condition.getTab();
        Long personId = condition.getPersonId();

        List<Comment> content = new ArrayList<>();
        Long total_ = 0L;


        Page<Comment> comments = new PageImpl<>(content, pageable, total_);

        if (keyword == null || keyword.isBlank()) {
            comments = commentRepository.searchAll(pageable);
        } else {
            switch (tab) {
                case 1:
                    comments = commentRepository.searchByContentOrPersonName(keyword, pageable);
                    break;
                case 2:
                    comments = commentRepository.searchByPersonName(keyword, personId, pageable);
                    break;
                case 3:
                    comments = commentRepository.searchByTagName(keyword, pageable);
                    break;
            }
        }


        List<CommentResponseDto> responseDtoList = comments.stream()
                .map(comment -> CommentResponseDto.builder()
                    .commentId(comment.getId())
                        .content(comment.getContent())
                        .commentDate(comment.getCommentDate())
                        .commentDate_format(comment.getCommentDate())
                        .status(comment.getStatus())
                        .createdDate(comment.getCreatedDate())
                        .updatedDate(comment.getUpdatedDate())
                        .sourceUrl(comment.getSourceUrl())
                        .sourceSort(comment.getSourceSort())
                        .person(new PersonResponseDto(comment.getPerson()))
                        .tags(commentRepository.getTags(comment.getId()))
                    .build())
                .collect(Collectors.toList());

        PageMetadata pageMetadata = new PageMetadata(comments);

        return new CommentListResponseDto(responseDtoList,pageMetadata);
    }



    public CommentResponseDto findById(Long id) {

        Comment entity = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트 정보가 없습니다. id = " + id));

        return new CommentResponseDto(entity);


    }
}