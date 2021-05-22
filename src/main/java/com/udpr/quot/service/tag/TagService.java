package com.udpr.quot.service.tag;

import com.udpr.quot.domain.tag.Tag;
import com.udpr.quot.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Tag findOrSaveTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElse(Tag.builder()
                        .name(tagName)
                        .build());
        return tagRepository.save(tag);
    }


}
