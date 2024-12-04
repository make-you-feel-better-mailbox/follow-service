package com.onetwo.followservice.adapter.out.persistence.repository.follow;

import com.onetwo.followservice.adapter.out.persistence.entity.FollowEntity;
import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;

import java.util.List;

public interface QFollowRepository {
    List<FollowEntity> sliceByCommand(FollowFilterCommand followFilterCommand);
}
