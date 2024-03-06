package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.RegisterFollowResponseDto;

public interface RegisterFollowUseCase {

    /**
     * Register follow use case,
     * register follow data to persistence
     *
     * @param registerFollowCommand data about register follow with user id and target user id
     * @return Boolean about register success
     */
    RegisterFollowResponseDto registerFollow(RegisterFollowCommand registerFollowCommand);
}
