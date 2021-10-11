package com.udpr.quot.domain.user;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkLike;
import com.udpr.quot.domain.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ColumnDefault("'anonymous'")
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @ColumnDefault("'/css/images/profile/basic_profile.png'")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Remark> remarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RemarkLike> remarkLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserScrap> userScrapList = new ArrayList<>();

    @Builder
    public User(String email, Role role, String nickname) {
        /*this.name = name;*/
        this.email = email;
        /*this.picture = picture;*/
        this.nickname = nickname;
        this.role = role;
    }

    /*public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }*/

    public String getRoleKey(){
        return this.role.getKey();
    }
}
