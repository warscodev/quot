package com.udpr.quot.domain.reporting;

import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reporting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String originContent;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remark_id")
    private Remark remark;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Reporting(String type, String originContent, String message, User reporter, Comment comment) {
        this.type = type;
        this.originContent = originContent;
        this.message = message;
        this.reporter = reporter;
        this.comment = comment;
    }

}
