package com.udpr.quot.web.dto.remark.comment;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private Remark remark;
    private User user;
    private Comment parent;
    private Comment ancestor;
    private Long parentId;
    private Long ancestorId;

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .remark(remark)
                .user(user)
                .parent(parent)
                .ancestor(ancestor)
                .build();
    }

}
