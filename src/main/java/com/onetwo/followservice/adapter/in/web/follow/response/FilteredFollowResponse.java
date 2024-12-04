package com.onetwo.followservice.adapter.in.web.follow.response;

import java.time.Instant;

public record FilteredFollowResponse(long followId,
                                     String follower,
                                     String followee,
                                     Instant createdDate) {
}
