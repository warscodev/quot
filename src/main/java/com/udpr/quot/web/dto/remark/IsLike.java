package com.udpr.quot.web.dto.remark;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsLike {
    int isLike;

    @QueryProjection
    public IsLike(int isLike){
        this.isLike = isLike;
    }
}
