package com.onetwo.followservice.application.port.in.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import onetwo.mailboxcommonconfig.common.SelfValidating;

@Getter
public final class DeleteFollowCommand extends SelfValidating<DeleteFollowCommand> {

    @NotEmpty
    private final String follower;

    @NotEmpty
    private final String followee;

    public DeleteFollowCommand(String follower, String followee) {
        this.follower = follower;
        this.followee = followee;
        this.validateSelf();
    }
}
