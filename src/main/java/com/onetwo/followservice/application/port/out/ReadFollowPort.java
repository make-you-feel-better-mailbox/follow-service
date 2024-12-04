package com.onetwo.followservice.application.port.out;

import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;
import com.onetwo.followservice.domain.Follow;

import java.util.List;
import java.util.Optional;

public interface ReadFollowPort {
    Optional<Follow> findByFollowerAndFollowee(String follower, String followee);

    long countFollowByFollowee(String targetUserId);

    long countFollowByFollower(String targetUserId);

    List<Follow> filterFollow(FollowFilterCommand followFilterCommand);
}
