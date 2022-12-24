package com.udpr.quot.domain.person.icon;

import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.common.Category;
import com.udpr.quot.domain.person.Person;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(catalog = "udpr_quot")
@Entity
public class Icon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icon_id")
    private Long id;

    private String path;

    private String category;

    private String iconTags;

    @OneToMany(mappedBy = "icon", fetch = FetchType.LAZY)
    private final List<Person> personList = new ArrayList<>();

    @Builder
    public Icon(String path, String category) {
        this.path = path;
        this.category = category;
    }

    public void update(String path, String category) {
        this.path = path;
        this.category = category;
    }

    public void update(String category) {
        this.category = category;
    }




}
