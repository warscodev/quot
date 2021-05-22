package com.udpr.quot.web.dto.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.tag.TagName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "발언내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "발언일자를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate commentDate;
    private String sourceSort;
    private String sourceUrl;
    private List<String> tags = new ArrayList<>();
    private String jasonArrayTags;
    private Person person;


    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .commentDate(commentDate)
                .person(person)
                .sourceSort(sourceSort)
                .sourceUrl(sourceUrl)
                .build();
    }

    public void jsonArrayToSet() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();

        //태그값들을 받아 온 경우에만 진행
        if(!this.getJasonArrayTags().isEmpty()) {
            //json으로 받아온 태그들을 TagName 객체 타입의 List로 변환
            List<TagName> tagsNameList = objectMapper
                    .readValue(this.getJasonArrayTags(), new TypeReference<>() {
                    });

            //변환된 List에서 태그값들을 꺼내 List<String> tags에 저장
            tagsNameList.forEach(tagName -> this.tags.add(tagName.getValue()));
        }
    }

}
