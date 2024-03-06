package com.onetwo.followservice.application.service.service;

import com.onetwo.followservice.application.port.in.command.CountFollowCommand;
import com.onetwo.followservice.application.port.in.command.DeleteFollowCommand;
import com.onetwo.followservice.application.port.in.command.FollowTargetCheckCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.CountFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.DeleteFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.FollowTargetCheckResponseDto;
import com.onetwo.followservice.application.port.in.response.RegisterFollowResponseDto;
import com.onetwo.followservice.application.port.in.usecase.DeleteFollowUseCase;
import com.onetwo.followservice.application.port.in.usecase.ReadFollowUseCase;
import com.onetwo.followservice.application.port.in.usecase.RegisterFollowUseCase;
import com.onetwo.followservice.application.port.out.ReadFollowPort;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.application.port.out.UpdateFollowPort;
import com.onetwo.followservice.application.service.converter.FollowUseCaseConverter;
import com.onetwo.followservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.followservice.domain.Follow;
import lombok.RequiredArgsConstructor;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService implements RegisterFollowUseCase, DeleteFollowUseCase, ReadFollowUseCase {

    private final ReadFollowPort readFollowPort;
    private final RegisterFollowPort registerFollowPort;
    private final UpdateFollowPort updateFollowPort;
    private final FollowUseCaseConverter followUseCaseConverter;

    /**
     * Register follow use case,
     * register follow data to persistence
     *
     * @param registerFollowCommand data about register follow with follower user id and followee user id
     * @return Boolean about register success
     */
    @Override
    @Transactional
    public RegisterFollowResponseDto registerFollow(RegisterFollowCommand registerFollowCommand) {
        Optional<Follow> optionalFollow = readFollowPort.findByFollowerAndFollowee(
                registerFollowCommand.getFollower(),
                registerFollowCommand.getFollowee()
        );

        if (optionalFollow.isPresent())
            throw new ResourceAlreadyExistsException("follow already exist. user can follow target user only ones");

        Follow newFollow = Follow.createNewFollowByCommand(registerFollowCommand);

        Follow savedFollow = registerFollowPort.registerFollow(newFollow);

        return followUseCaseConverter.followToRegisterResponseDto(savedFollow);
    }

    /**
     * Delete follow use case,
     * delete follow data to persistence
     *
     * @param deleteFollowCommand request delete data about follow follower, followee
     * @return Boolean about delete follow success
     */
    @Override
    @Transactional
    public DeleteFollowResponseDto deleteFollow(DeleteFollowCommand deleteFollowCommand) {
        Optional<Follow> optionalFollow = readFollowPort.findByFollowerAndFollowee(
                deleteFollowCommand.getFollower(),
                deleteFollowCommand.getFollowee()
        );

        if (optionalFollow.isEmpty()) throw new NotFoundResourceException("follow does not exist");

        Follow follow = optionalFollow.get();

        if (!follow.isSameUserId(deleteFollowCommand.getFollower()))
            throw new BadRequestException("Follow Register does not match with request user");

        follow.deleteFollow();

        updateFollowPort.updateFollow(follow);

        return followUseCaseConverter.followToDeleteResponseDto(follow);
    }

    /**
     * Get Target's Follow count use case,
     * get target's Follow count on persistence
     *
     * @param countFollowCommand request count target data
     * @return About target's Follow count
     */
    @Override
    @Transactional(readOnly = true)
    public CountFollowResponseDto getFollowCount(CountFollowCommand countFollowCommand) {
        long followerCount = readFollowPort.countFollowByFollowee(countFollowCommand.getTargetUserId());

        long followeeCount = readFollowPort.countFollowByFollower(countFollowCommand.getTargetUserId());

        return followUseCaseConverter.resultToCountResponseDto(followerCount, followeeCount);
    }

    /**
     * Get Boolean about User follow Target user use case
     *
     * @param followTargetCheckCommand request target data and user data
     * @return Boolean about user follow target user
     */
    @Override
    public FollowTargetCheckResponseDto userFollowTargetUserCheck(FollowTargetCheckCommand followTargetCheckCommand) {
        Optional<Follow> optionalFollow = readFollowPort.findByFollowerAndFollowee(
                followTargetCheckCommand.getFollower(),
                followTargetCheckCommand.getFollowee()
        );

        return followUseCaseConverter.resultToFollowTargetCheckResponseDto(optionalFollow.isPresent());
    }
}
