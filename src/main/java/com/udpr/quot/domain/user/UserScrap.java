package com.udpr.quot.domain.user;

import com.udpr.quot.domain.remark.Remark;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_scrap_id")
    private Long id;

   /* @ManyToOne
    private User user;

    @ManyToOne
    private Remark remark;*/


}
