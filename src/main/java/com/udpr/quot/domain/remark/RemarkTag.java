package com.udpr.quot.domain.remark;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.udpr.quot.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(catalog = "udpr_quot")
@Entity
public class RemarkTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remark_tag_id")
    private Long id;

    @JsonBackReference(value = "tag")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remark_id")
    private Remark remark;


    @Builder
    public RemarkTag(Tag tag, Remark remark) {
        this.tag = tag;
        this.remark = remark;
    }
}
