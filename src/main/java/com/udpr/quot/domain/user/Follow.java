package com.udpr.quot.domain.user;

import com.udpr.quot.domain.person.Person;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(catalog = "udpr_quot")
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Follow(Person person, User user) {
        this.person = person;
        this.user = user;
    }
}
