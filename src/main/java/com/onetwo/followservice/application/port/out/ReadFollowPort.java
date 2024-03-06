package com.onetwo.followservice.application.port.out;

import com.onetwo.followservice.domain.Follow;

import java.util.Optional;

public interface ReadFollowPort {
    Optional<Follow> findByFollowerAndFollowee(String follower, String followee);

    long countFollowByFollowee(String targetUserId);

    long countFollowByFollower(String targetUserId);
}
