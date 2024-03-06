package com.onetwo.followservice.adapter.out.persistence.repository.follow;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class QFollowRepositoryImpl extends QuerydslRepositorySupport implements QFollowRepository {

    private final JPAQueryFactory factory;

    public QFollowRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(FollowEntity.class);
        this.factory = jpaQueryFactory;
    }
}
