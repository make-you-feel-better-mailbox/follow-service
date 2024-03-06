package com.onetwo.followservice.adapter.out.persistence.repository.follow;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long>, QFollowRepository {
    Optional<FollowEntity> findByFollowerAndFolloweeAndState(String userId, String targetUserId, boolean persistenceNotDeleted);

    Long countByFollowerAndState(String targetUserId, boolean persistenceNotDeleted);

    Long countByFolloweeAndState(String targetUserId, boolean persistenceNotDeleted);
}
