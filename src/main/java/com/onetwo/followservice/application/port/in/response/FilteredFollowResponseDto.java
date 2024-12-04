package com.onetwo.followservice.application.port.in.response;

import java.time.Instant;

public record FilteredFollowResponseDto(long followId,
                                        String follower,
                                        String followee,
                                        Instant createdDate) {
}
