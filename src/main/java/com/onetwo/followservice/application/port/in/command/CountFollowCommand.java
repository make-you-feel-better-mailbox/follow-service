package com.onetwo.followservice.application.port.in.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import onetwo.mailboxcommonconfig.common.SelfValidating;

@Getter
public final class CountFollowCommand extends SelfValidating<CountFollowCommand> {

    @NotEmpty
    private final String targetUserId;

    public CountFollowCommand(String targetUserId) {
        this.targetUserId = targetUserId;
        this.validateSelf();
    }
}
