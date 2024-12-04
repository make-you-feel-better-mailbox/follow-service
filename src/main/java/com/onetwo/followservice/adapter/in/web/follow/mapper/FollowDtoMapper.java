package com.onetwo.followservice.adapter.in.web.follow.mapper;

import com.onetwo.followservice.adapter.in.web.follow.request.RegisterFollowRequest;
import com.onetwo.followservice.adapter.in.web.follow.response.CountFollowResponse;
import com.onetwo.followservice.adapter.in.web.follow.response.DeleteFollowResponse;
import com.onetwo.followservice.adapter.in.web.follow.response.FollowTargetCheckResponse;
import com.onetwo.followservice.adapter.in.web.follow.response.RegisterFollowResponse;
import com.onetwo.followservice.application.port.in.command.CountFollowCommand;
import com.onetwo.followservice.application.port.in.command.DeleteFollowCommand;
import com.onetwo.followservice.application.port.in.command.FollowTargetCheckCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.CountFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.DeleteFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.FollowTargetCheckResponseDto;
import com.onetwo.followservice.application.port.in.response.RegisterFollowResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FollowDtoMapper {

    public RegisterFollowCommand registerRequestToCommand(String userId, RegisterFollowRequest registerFollowRequest) {
        return new RegisterFollowCommand(userId, registerFollowRequest.targetUserId());
    }

    public RegisterFollowResponse dtoToRegisterResponse(RegisterFollowResponseDto registerFollowResponseDto) {
        return new RegisterFollowResponse(registerFollowResponseDto.isRegisterSuccess());
    }

    public DeleteFollowCommand deleteRequestToCommand(String userId, String targetUserId) {
        return new DeleteFollowCommand(userId, targetUserId);
    }

    public DeleteFollowResponse dtoToDeleteResponse(DeleteFollowResponseDto deleteFollowResponseDto) {
        return new DeleteFollowResponse(deleteFollowResponseDto.isDeleteSuccess());
    }

    public CountFollowCommand countRequestToCommand(String targetUserId) {
        return new CountFollowCommand(targetUserId);
    }

    public CountFollowResponse dtoToCountResponse(CountFollowResponseDto countFollowResponseDto) {
        return new CountFollowResponse(countFollowResponseDto.followerCount(), countFollowResponseDto.followeeCount());
    }

    public FollowTargetCheckCommand followCheckRequestToCommand(String userId, String targetUserId) {
        return new FollowTargetCheckCommand(userId, targetUserId);
    }

    public FollowTargetCheckResponse dtoToFollowTargetCheckResponse(FollowTargetCheckResponseDto followTargetCheckResponseDto) {
        return new FollowTargetCheckResponse(followTargetCheckResponseDto.isUserFollowTargetUser());
    }
}
