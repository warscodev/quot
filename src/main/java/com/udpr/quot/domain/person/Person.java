package com.udpr.quot.domain.person;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.comment.Comment;
import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.common.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SQLDelete(sql = "UPDATE person SET status = 'DELETED' WHERE person_id = ?")
@Entity
public class Person extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    private String name;

    @Embedded
    private Birth birth;

    private String job;

    private String gender;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    private Status status; //CREATED, DELETED, UPDATED

    private String category;

    @JsonManagedReference(value = "person")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();


    @Builder
    public Person(String name, Birth birth, String job,
                  String gender, String summary, String category) {
        this.name = name.trim();
        this.birth = birth;
        this.job = job;
        this.gender = gender;
        this.summary = summary;
        this.status = Status.CREATED;
        this.category = category;
    }

    public void update(String name, Birth birth, String job, String gender, String summary, String category){
        this.name = name.trim();
        this.birth = birth;
        this.job = job;
        this.gender = gender;
        this.summary = summary;
        this.status = Status.UPDATED;
        this.category = category;
    }
}
