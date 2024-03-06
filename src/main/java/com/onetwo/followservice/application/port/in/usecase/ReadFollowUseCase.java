package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.CountFollowCommand;
import com.onetwo.followservice.application.port.in.command.FollowTargetCheckCommand;
import com.onetwo.followservice.application.port.in.response.CountFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.FollowTargetCheckResponseDto;

public interface ReadFollowUseCase {

    /**
     * Get Target's Follow count use case,
     * get target's Follow count on persistence
     *
     * @param countFollowCommand request count target data
     * @return About target's Follow count
     */
    CountFollowResponseDto getFollowCount(CountFollowCommand countFollowCommand);

    /**
     * Get Boolean about User follow Target user use case
     *
     * @param followTargetCheckCommand request target data and user data
     * @return Boolean about user follow target user
     */
    FollowTargetCheckResponseDto userFollowTargetUserCheck(FollowTargetCheckCommand followTargetCheckCommand);
}