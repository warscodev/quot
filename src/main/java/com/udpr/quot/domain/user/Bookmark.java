package com.udpr.quot.domain.user;

import com.udpr.quot.domain.remark.Remark;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(catalog = "udpr_quot")
@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "remark_id")
    private Remark remark;

    @Builder
    public Bookmark(User user, Remark remark) {
        this.user = user;
        this.remark = remark;
    }
}
