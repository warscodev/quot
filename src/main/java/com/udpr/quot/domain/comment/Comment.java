package com.udpr.quot.domain.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate commentDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String sourceSort;

    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    @JsonBackReference(value = "person")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @JsonManagedReference(value = "comment")
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentTag> commentTagList = new ArrayList<>();


    @Builder
    public Comment(String content, LocalDate commentDate, Person person, String sourceSort, String sourceUrl) {
        this.content = content;
        this.commentDate = commentDate;
        this.status = Status.CREATED;
        this.person = person;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }

    public void update(String content, LocalDate commentDate, String sourceSort,
                       String sourceUrl) {
        this.content = content;
        this.commentDate = commentDate;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
        this.status = Status.UPDATED;
    }

    public List<String> TagsToStringList() {

        List<String> tagList = new ArrayList<>();

        this.getCommentTagList().stream().forEach(
                commentTag -> tagList.add(commentTag.getTag().getName())
        );

        return tagList;
    }

    public List<Tag> toTagList(){

        System.out.println("toTagList 작동");
        List<Tag> tagList = new ArrayList<>();

        this.getCommentTagList().stream().forEach(
                commentTag -> tagList.add(commentTag.getTag())
        );

        return tagList;

    }


}