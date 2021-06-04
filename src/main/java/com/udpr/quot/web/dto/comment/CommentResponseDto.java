package com.udpr.quot.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.domain.comment.CommentTag;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.tag.Tag;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import com.udpr.quot.web.dto.tag.TagDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String content;
    private LocalDate commentDate;
    private String commentDate_format;
    private Status status;
    private String createdDate;
    private String updatedDate;
    @JsonIgnore
    private List<CommentTag> commentTagList = new ArrayList<>();
    private PersonResponseDto person;
    private List<TagDto> tags = new ArrayList<>();
    private String sourceSort;
    private String sourceUrl;


    /*@QueryProjection
    public CommentResponseDto(Long commentId, String content, LocalDate commentDate, Status status,
                              LocalDateTime createdDate, LocalDateTime updatedDate, Person person,
                              List<CommentTag> commentTagList,
                              String sourceSort, String sourceUrl) {
        this.commentId = commentId;
        this.content = content;
        this.commentDate = commentDate;
        this.person = person;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        //this.commentTagList = commentTagList;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }*/

    @Builder
    public CommentResponseDto(Long commentId, String content, LocalDate commentDate, LocalDate commentDate_format, Status status,
                              LocalDateTime createdDate, LocalDateTime updatedDate,
                              List<CommentTag> commentTagList, PersonResponseDto person,
                              String sourceSort, String sourceUrl, List<TagDto> tags) {
        this.commentId = commentId;
        this.content = content;
        this.commentDate = commentDate;
        this.commentDate_format = commentDateFormat(commentDate_format);
        this.status = status;
        this.createdDate = formatDate(createdDate);
        this.updatedDate = formatDate(updatedDate);
        this.commentTagList = commentTagList;
        this.tags = tags;
        this.person = person;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }

    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getId();
        this.content = entity.getContent();
        this.commentDate = entity.getCommentDate();
        this.createdDate = formatDate(entity.getCreatedDate());
        this.updatedDate = formatDate(entity.getUpdatedDate());
        this.person = new PersonResponseDto(entity.getPerson());
        this.status = entity.getStatus();
        this.commentTagList = entity.getCommentTagList();
        this.sourceSort = entity.getSourceSort();
        this.sourceUrl = entity.getSourceUrl();
        this.tags = setTagsList();
    }

    public List<TagDto> setTagsList() {

        List<Tag> tags = getCommentTagList().stream()
                .map(commentTag -> commentTag.getTag())
                .collect(Collectors.toList());

        return tags.stream().map(TagDto::new).collect(Collectors.toList());
    }

    public String formatDate(LocalDateTime localDateTime){

        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public String commentDateFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }


}
