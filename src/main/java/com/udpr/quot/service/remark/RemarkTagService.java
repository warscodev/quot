package com.udpr.quot.service.remark;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkTag;
import com.udpr.quot.domain.remark.repository.RemarkTagRepository;
import com.udpr.quot.domain.tag.Tag;
import com.udpr.quot.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RemarkTagService {

    private final RemarkTagRepository remarkTagRepository;
    private final TagService tagService;

    @Transactional
    public void saveTags(Remark remark, List<String> tags) {

        //Remark의 RemarkTagList가 비어있는지 확인
        //비어있지 않다면 RemarkTagList를 비우고 태그를 저장
        if(!remark.getRemarkTagList().isEmpty()) {
            remarkTagRepository.deleteByRemarkId(remark.getId());
        }

        tags.forEach(tagName -> {
            Tag tag = tagService.findOrSaveTag(tagName);
            saveRemarkTag(remark, tag);
        });
    }

    @Transactional
    public void saveRemarkTag(Remark remark, Tag tag) {
        RemarkTag remarkTag = RemarkTag.builder()
                .remark(remark)
                .tag(tag)
                .build();
        remarkTagRepository.save(remarkTag);
    }



}
