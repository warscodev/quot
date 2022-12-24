package com.udpr.quot.domain.remark;

import com.udpr.quot.domain.common.BaseTimeEntity;
import com.udpr.quot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Table(catalog = "udpr_quot")
@Entity
public class RemarkLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remark_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remark_id")
    private Remark remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int isLike;

    @Builder
    public RemarkLike(Remark remark, User user, int isLike) {
        this.remark = remark;
        this.user = user;
        this.isLike = isLike;
    }

    public void update(int isLike){
        this.isLike = isLike;
    }
}
