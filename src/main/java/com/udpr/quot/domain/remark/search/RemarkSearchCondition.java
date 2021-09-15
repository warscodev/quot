package com.udpr.quot.domain.remark.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class RemarkSearchCondition {

    String keyword;

    int tab;

    long personId = 0L;

    String sort;

    Long sid;
}
