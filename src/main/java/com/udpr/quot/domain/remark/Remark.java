package com.udpr.quot.domain.remark;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Remark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remark_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate remarkDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String sourceSort;

    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    @JsonBackReference(value = "person")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @JsonManagedReference(value = "remark")
    @OneToMany(mappedBy = "remark", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RemarkTag> remarkTagList = new ArrayList<>();

    @OneToMany(mappedBy = "remark", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RemarkLike> remarkLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "remark", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @ColumnDefault("0")
    private int likeCount;

    @ColumnDefault("0")
    private int dislikeCount;

    @Formula("(select count(*) from comment where comment.remark_id=remark_id and comment.status <> 'DELETED')")
    private int commentCount;

    @JsonBackReference(value = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Remark(String content, LocalDate remarkDate, Person person, User user, String sourceSort, String sourceUrl) {
        this.content = content.trim();
        this.remarkDate = remarkDate;
        this.status = Status.CREATED;
        this.person = person;
        this.user = user;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
    }

    public void update(String content, LocalDate remarkDate, String sourceSort,
                       String sourceUrl) {
        this.content = content.trim();
        this.remarkDate = remarkDate;
        this.sourceSort = sourceSort;
        this.sourceUrl = sourceUrl;
        this.status = Status.UPDATED;
    }

    public List<String> TagsToStringList() {
        List<String> tagList = new ArrayList<>();
        this.getRemarkTagList().forEach(
                remarkTag -> tagList.add(remarkTag.getTag().getName())
        );
        return tagList;
    }

    public void increaseLikeCount(int isLike){
        if(isLike == 1) {
            this.likeCount++;
        }else{
            this.dislikeCount++;
        }
    }

    public void decreaseLikeCount(int isLike){
        if(isLike == 1) {
            this.likeCount--;
        }else{
            this.dislikeCount--;
        }
    }

}