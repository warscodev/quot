package com.udpr.quot.domain.person;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.remark.Remark;
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
@Entity
public class Person extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    private String name;

    private String alias;

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
    private List<Remark> remarkList = new ArrayList<>();


    @Builder
    public Person(String name, String alias, Birth birth, String job,
                  String gender, String summary, String category) {
        this.name = name.trim();
        this.alias = alias.trim();
        this.birth = birth;
        this.job = job.trim();
        this.gender = gender;
        this.summary = summary.trim();
        this.status = Status.CREATED;
        this.category = category;
    }

    public void update(String name, String alias, Birth birth, String job, String gender, String summary, String category){
        this.name = name.trim();
        this.alias = alias.trim();
        this.birth = birth;
        this.job = job.trim();
        this.gender = gender;
        this.summary = summary.trim();
        this.status = Status.UPDATED;
        this.category = category;
    }
}
