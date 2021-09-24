package com.udpr.quot.domain.remark.search;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemarkSearchCondition {

    String keyword;

    int tab;

    long personId;


    String sort;

    //session Id
    Long sid;

    int page;

    public RemarkSearchCondition() {
        this.tab = 1;
        this.personId = 0L;
        this.sort = "cd_d";
        this.page = 1;
    }
}
