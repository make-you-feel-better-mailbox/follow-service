package com.onetwo.followservice.application.service.adapter;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import com.onetwo.followservice.adapter.out.persistence.repository.follow.FollowRepository;
import com.onetwo.followservice.application.port.out.ReadFollowPort;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.application.port.out.UpdateFollowPort;
import com.onetwo.followservice.common.GlobalStatus;
import com.onetwo.followservice.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements ReadFollowPort, RegisterFollowPort, UpdateFollowPort {

    private final FollowRepository followRepository;

    @Override
    public Optional<Follow> findByFollowerAndFollowee(String follower, String followee) {
        Optional<FollowEntity> optionalFollowEntity = followRepository.findByFollowerAndFolloweeAndState(
                follower,
                followee,
                GlobalStatus.PERSISTENCE_NOT_DELETED
        );

        if (optionalFollowEntity.isPresent()) {
            Follow follow = Follow.entityToDomain(optionalFollowEntity.get());

            return Optional.of(follow);
        }

        return Optional.empty();
    }

    @Override
    public Follow registerFollow(Follow newFollow) {
        FollowEntity followEntity = FollowEntity.domainToEntity(newFollow);

        FollowEntity savedFollowEntity = followRepository.save(followEntity);

        return Follow.entityToDomain(savedFollowEntity);
    }

    @Override
    @Transactional
    public void updateFollow(Follow follow) {
        FollowEntity followEntity = FollowEntity.domainToEntity(follow);
        followRepository.save(followEntity);
    }

    @Override
    public long countFollowByFollowee(String targetUserId) {
        Long countFollowee = followRepository.countByFollowerAndState(targetUserId, GlobalStatus.PERSISTENCE_NOT_DELETED);

        return countFollowee == null ? 0L : countFollowee;
    }

    @Override
    public long countFollowByFollower(String targetUserId) {
        Long countFollower = followRepository.countByFolloweeAndState(targetUserId, GlobalStatus.PERSISTENCE_NOT_DELETED);

        return countFollower == null ? 0L : countFollower;
    }
}
