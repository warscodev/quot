package com.udpr.quot.web.dto.remark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkTag;
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
public class RemarkResponseDto {

    private Long remarkId;
    private String content;
    private LocalDate remarkDate;
    private String remarkDate_format;
    private Status status;
    private String createdDate;
    private String updatedDate;
    @JsonIgnore
    private List<RemarkTag> remarkTagList = new ArrayList<>();
    private PersonResponseDto person;
    private List<TagDto> tags = new ArrayList<>();
    private String sourceSort;
    private String sourceUrl;


    /*@QueryProjection
    public RemarkResponseDto(Long remarkId, String content, LocalDate remarkDate, Status status,
                              LocalDateTime createdDate, LocalDateTime updatedDate, Person person,
                              List<RemarkTag> remarkTagList,
                              String sourceSort, String sourceUrl) {
        this.remarkId = remarkId;
        this.content = content;
        this.remarkDate = remarkDate;
        this.person = person;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        //this.remarkTagList = remarkTagList;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }*/

    @Builder
    public RemarkResponseDto(Long remarkId, String content, LocalDate remarkDate, LocalDate remarkDate_format, Status status,
                             LocalDateTime createdDate, LocalDateTime updatedDate,
                             List<RemarkTag> remarkTagList, PersonResponseDto person,
                             String sourceSort, String sourceUrl, List<TagDto> tags) {
        this.remarkId = remarkId;
        this.content = content;
        this.remarkDate = remarkDate;
        this.remarkDate_format = remarkDateFormat(remarkDate_format);
        this.status = status;
        this.createdDate = formatDate(createdDate);
        this.updatedDate = formatDate(updatedDate);
        this.remarkTagList = remarkTagList;
        this.tags = tags;
        this.person = person;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }

    public RemarkResponseDto(Remark entity) {
        this.remarkId = entity.getId();
        this.content = entity.getContent();
        this.remarkDate = entity.getRemarkDate();
        this.createdDate = formatDate(entity.getCreatedDate());
        this.updatedDate = formatDate(entity.getUpdatedDate());
        this.person = new PersonResponseDto(entity.getPerson());
        this.status = entity.getStatus();
        this.remarkTagList = entity.getRemarkTagList();
        this.sourceSort = entity.getSourceSort();
        this.sourceUrl = entity.getSourceUrl();
        this.tags = setTagsList();
    }

    public List<TagDto> setTagsList() {

        List<Tag> tags = getRemarkTagList().stream()
                .map(remarkTag -> remarkTag.getTag())
                .collect(Collectors.toList());

        return tags.stream().map(TagDto::new).collect(Collectors.toList());
    }

    public String formatDate(LocalDateTime localDateTime){

        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public String remarkDateFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }


}
