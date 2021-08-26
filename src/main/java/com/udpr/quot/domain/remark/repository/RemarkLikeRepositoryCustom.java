package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.user.User;

public interface RemarkLikeRepositoryCustom {
    int getIsLike(Remark remark, User user);

    void update(Remark remark, User user, int isLike);
}
