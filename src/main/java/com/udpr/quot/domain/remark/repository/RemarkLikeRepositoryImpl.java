package com.udpr.quot.domain.remark.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkLike;
import com.udpr.quot.domain.user.User;

import static com.udpr.quot.domain.remark.QRemarkLike.remarkLike;


import javax.persistence.EntityManager;

public class RemarkLikeRepositoryImpl implements RemarkLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RemarkLikeRepositoryImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public int getIsLike(Remark remark, User user){
        QueryResults<Integer> results = queryFactory
                .select(remarkLike.isLike)
                .from(remarkLike)
                .where(remarkLike.remark.eq(remark), remarkLike.user.eq(user))
                .fetchResults();

        return results.getResults().get(0);
    }

    @Override
    public void update(Remark remark, User user, int isLike){
        queryFactory
                .update(remarkLike)
                .where(remarkLike.remark.eq(remark), remarkLike.user.eq(user))
                .set(remarkLike.isLike, isLike)
                .execute();
    }

}
