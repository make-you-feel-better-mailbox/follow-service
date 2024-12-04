package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.CountFollowCommand;
import com.onetwo.followservice.application.port.in.command.FollowTargetCheckCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.CountFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.FollowTargetCheckResponseDto;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.domain.Follow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReadFollowUseCaseBootTest {

    @Autowired
    private ReadFollowUseCase readFollowUseCase;

    @Autowired
    private RegisterFollowPort registerFollowPort;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";
    private final long followerCount = 12L;
    private final long followeeCount = 7L;

    @Test
    @Transactional
    @DisplayName("[통합][Use Case] Follow 갯수 조회 - 성공 테스트")
    void countFollowUseCaseSuccessTest() {
        //given
        CountFollowCommand CountFollowCommand = new CountFollowCommand(followee);

        for (int i = 0; i <= followeeCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower + i, followee);
            Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

            registerFollowPort.registerFollow(follow);
        }

        for (int i = 0; i <= followerCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(followee, follower + i);
            Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

            registerFollowPort.registerFollow(follow);
        }

        //when
        CountFollowResponseDto result = readFollowUseCase.getFollowCount(CountFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.followeeCount() >= followeeCount);
        Assertions.assertTrue(result.followerCount() >= followerCount);
    }

    @Test
    @Transactional
    @DisplayName("[통합][Use Case] Follow 등록 여부 조회 - 성공 테스트")
    void userTargetFollowCheckUseCaseSuccessTest() {
        //given
        FollowTargetCheckCommand followTargetCheckCommand = new FollowTargetCheckCommand(follower, followee);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        registerFollowPort.registerFollow(follow);

        //when
        FollowTargetCheckResponseDto result = readFollowUseCase.userFollowTargetUserCheck(followTargetCheckCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isUserFollowTargetUser());
    }
}