package com.onetwo.followservice.application.service.adapter;

import com.onetwo.followservice.application.port.out.FollowLockPort;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowLockAdapter implements FollowLockPort {

    private final RedissonClient redissonClient;

    @Override
    public RLock getFollowRock(String follower, String followee) {

        String lockKey = "followLock-follower" + follower +
                "followee" + followee;

        return redissonClient.getLock(lockKey);
    }
}
