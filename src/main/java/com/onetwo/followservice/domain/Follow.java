package com.onetwo.followservice.domain;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Follow extends BaseDomain {

    private Long id;
    // follower user id
    private String follower;
    // followee user id
    private String followee;
    private Boolean state;

    public static Follow createNewFollowByCommand(RegisterFollowCommand registerFollowCommand) {
        Follow follow = new Follow(
                null,
                registerFollowCommand.getFollower(),
                registerFollowCommand.getFollowee(),
                false
        );

        follow.setDefaultState();
        return follow;
    }

    public static Follow entityToDomain(FollowEntity followEntity) {
        Follow follow = new Follow(
                followEntity.getId(),
                followEntity.getFollower(),
                followEntity.getFollowee(),
                followEntity.getState()
        );

        follow.setMetaDataByEntity(followEntity);
        return follow;
    }

    private void setDefaultState() {
        setCreatedAt(Instant.now());
        setCreateUser(this.follower);
    }

    public void deleteFollow() {
        this.state = true;
        setUpdatedAt(Instant.now());
        setUpdateUser(this.follower);
    }


    public boolean isDeleted() {
        return this.state;
    }

    public boolean isSameUserId(String follower) {
        return this.follower.equals(follower);
    }
}
