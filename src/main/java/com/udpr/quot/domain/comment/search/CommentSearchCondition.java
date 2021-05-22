package com.udpr.quot.domain.comment.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CommentSearchCondition {

    String keyword;

    int tab;

    long personId = 0L;
}
