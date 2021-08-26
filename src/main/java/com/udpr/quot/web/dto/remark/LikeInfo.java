package com.udpr.quot.web.dto.remark;

import lombok.Getter;

@Getter
public class LikeInfo {

    private int likeCount;
    private int dislikeCount;
    private int isLike;

    public LikeInfo(int likeCount, int dislikeCount, int isLike) {
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.isLike = isLike;
    }
}
