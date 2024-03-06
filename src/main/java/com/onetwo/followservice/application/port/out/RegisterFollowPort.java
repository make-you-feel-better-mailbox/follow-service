package com.onetwo.followservice.application.port.out;

import com.onetwo.followservice.domain.Follow;

public interface RegisterFollowPort {
    Follow registerFollow(Follow newFollow);
}
