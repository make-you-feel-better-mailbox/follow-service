package com.onetwo.followservice.adapter.out.persistence.repository.follow;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;
import com.onetwo.followservice.common.GlobalStatus;
import com.onetwo.followservice.common.utils.QueryDslUtil;
import com.onetwo.followservice.common.utils.SliceUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.onetwo.followservice.adapter.out.persistence.entity.QFollowEntity.followEntity;

public class QFollowRepositoryImpl extends QuerydslRepositorySupport implements QFollowRepository {

    private final JPAQueryFactory factory;

    public QFollowRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(FollowEntity.class);
        this.factory = jpaQueryFactory;
    }

    @Override
    public List<FollowEntity> sliceByCommand(FollowFilterCommand followFilterCommand) {
        return factory.select(followEntity)
                .from(followEntity)
                .where(filterCondition(followFilterCommand),
                        followEntity.state.eq(GlobalStatus.PERSISTENCE_NOT_DELETED))
                .limit(SliceUtil.getSliceLimit(followFilterCommand.getPageable().getPageSize()))
                .offset(followFilterCommand.getPageable().getOffset())
                .orderBy(followEntity.id.desc())
                .fetch();
    }

    private Predicate filterCondition(FollowFilterCommand followFilterCommand) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QueryDslUtil.ifConditionExistAddEqualPredicate(followFilterCommand.getFollower(), followEntity.follower, booleanBuilder);
        QueryDslUtil.ifConditionExistAddEqualPredicate(followFilterCommand.getFollowee(), followEntity.followee, booleanBuilder);

        return booleanBuilder;
    }
}
