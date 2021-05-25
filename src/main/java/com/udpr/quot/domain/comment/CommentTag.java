package com.udpr.quot.domain.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.udpr.quot.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CommentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_tag_id")
    private Long id;

    @JsonBackReference(value = "tag")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @JsonBackReference(value = "comment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    @Builder
    public CommentTag(Tag tag, Comment comment) {
        this.tag = tag;
        this.comment = comment;
    }
}
