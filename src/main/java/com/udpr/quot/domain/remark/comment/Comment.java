package com.udpr.quot.domain.remark.comment;

import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private String deletedContent;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remark_id")
    private Remark remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestor_id")
    private Comment ancestor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder
    public Comment(String content, Remark remark, User user, Comment parent, Comment ancestor) {
        this.content = content.trim();
        this.remark = remark;
        this.user = user;
        this.parent = parent;
        this.ancestor = ancestor;
        this.status = Status.CREATED;
    }

    public void update(String content){
        this.content = content.trim();
        this.status = Status.UPDATED;
    }

    public void setStatusDeleted(){
        this.deletedContent = this.content;
        this.content = "[ 삭제된 댓글 입니다 ]";
        this.status = Status.DELETED;
    }

    public void setStatusDeletedAncestor(){
        this.deletedContent = this.content;
        this.content = "[ 삭제된 댓글 입니다 ]";
        this.status = Status.DELETED_ANCESTOR;
    }

}
