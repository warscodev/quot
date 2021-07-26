package com.udpr.quot.domain.tag;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.remark.RemarkTag;
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
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @JsonManagedReference(value = "tag")
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<RemarkTag> remarkTagList = new ArrayList<>();


    @Builder
    public Tag(String name) {
        this.name = name;
    }


}
