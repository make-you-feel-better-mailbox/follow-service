package com.onetwo.followservice.application.port.out;

import com.onetwo.followservice.domain.Follow;

public interface UpdateFollowPort {
    void updateFollow(Follow follow);
}
