package com.onetwo.followservice.application.port.in.command;

import com.onetwo.followservice.application.port.SliceRequest;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public final class FollowFilterCommand extends SliceRequest<FollowFilterCommand> {

    private final String follower;

    private final String followee;

    public FollowFilterCommand(Pageable pageable, String follower, String followee) {
        super(pageable);
        this.follower = follower;
        this.followee = followee;
        this.validateSelf();
    }
}
