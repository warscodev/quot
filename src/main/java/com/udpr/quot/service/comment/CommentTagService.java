package com.udpr.quot.service.comment;

import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.domain.comment.CommentTag;
import com.udpr.quot.domain.comment.repository.CommentTagRepository;
import com.udpr.quot.domain.tag.Tag;
import com.udpr.quot.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentTagService {

    private final CommentTagRepository commentTagRepository;
    private final TagService tagService;

    @Transactional
    public void saveTags(Comment comment, List<String> tags) {

        //Comment의 CommentTagList가 비어있는지 확인
        //비어있지 않다면 CommentTagList를 비우고 태그를 저장
        if(!comment.getCommentTagList().isEmpty()) {
            commentTagRepository.deleteByCommentId(comment.getId());
        }

        tags.forEach(tagName -> {
            Tag tag = tagService.findOrSaveTag(tagName);
            saveCommentTag(comment, tag);
        });
    }

    @Transactional
    public void saveCommentTag(Comment comment, Tag tag) {
        CommentTag commentTag = CommentTag.builder()
                .comment(comment)
                .tag(tag)
                .build();
        commentTagRepository.save(commentTag);
    }



}
