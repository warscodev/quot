package com.udpr.quot.domain.person;

import com.udpr.quot.domain.common.Status;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonSearchCondition {

    private String name;

    private String category;

    private String job;

    private Status status;

}
