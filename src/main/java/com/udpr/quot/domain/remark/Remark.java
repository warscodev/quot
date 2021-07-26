package com.udpr.quot.domain.remark;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.tag.Tag;
import com.udpr.quot.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("0")
    private int likeCount;

    @ColumnDefault("0")
    private int dislikeCount;

    @JsonBackReference(value = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Remark(String content, LocalDate remarkDate, Person person, String sourceSort, String sourceUrl) {
        this.content = content.trim();
        this.remarkDate = remarkDate;
        this.status = Status.CREATED;
        this.person = person;
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

        this.getRemarkTagList().stream().forEach(
                remarkTag -> tagList.add(remarkTag.getTag().getName())
        );

        return tagList;
    }

    public List<Tag> toTagList(){

        System.out.println("toTagList 작동");
        List<Tag> tagList = new ArrayList<>();

        this.getRemarkTagList().stream().forEach(
                remarkTag -> tagList.add(remarkTag.getTag())
        );

        return tagList;

    }


}