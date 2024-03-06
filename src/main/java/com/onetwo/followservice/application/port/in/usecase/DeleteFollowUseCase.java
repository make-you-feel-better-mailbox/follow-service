package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.DeleteFollowCommand;
import com.onetwo.followservice.application.port.in.response.DeleteFollowResponseDto;

public interface DeleteFollowUseCase {

    /**
     * Delete follow use case,
     * delete follow data to persistence
     *
     * @param deleteFollowCommand request delete data about follow follower, followee
     * @return Boolean about delete follow success
     */
    DeleteFollowResponseDto deleteFollow(DeleteFollowCommand deleteFollowCommand);
}
