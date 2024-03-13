package com.onetwo.followservice.application.port.out;

import org.redisson.api.RLock;

public interface FollowLockPort {

    RLock getFollowRock(String follower, String followee);
}
