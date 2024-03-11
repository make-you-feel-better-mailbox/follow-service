package com.onetwo.followservice.application.service.converter;

import com.onetwo.followservice.application.port.in.response.*;
import com.onetwo.followservice.domain.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowUseCaseConverter {
    public RegisterFollowResponseDto followToRegisterResponseDto(Follow savedFollow) {
        return new RegisterFollowResponseDto(savedFollow != null && savedFollow.getId() != null);
    }

    public DeleteFollowResponseDto followToDeleteResponseDto(Follow follow) {
        return new DeleteFollowResponseDto(follow.isDeleted());
    }

    public CountFollowResponseDto resultToCountResponseDto(long followerCount, long followeeCount) {
        return new CountFollowResponseDto(followerCount, followeeCount);
    }

    public FollowTargetCheckResponseDto resultToFollowTargetCheckResponseDto(boolean isUserFollowTargetUser) {
        return new FollowTargetCheckResponseDto(isUserFollowTargetUser);
    }

    public FilteredFollowResponseDto followToFilteredResponse(Follow follow) {
        return new FilteredFollowResponseDto(follow.getId(), follow.getFollower(), follow.getFollower(), follow.getCreatedAt());
    }
}
