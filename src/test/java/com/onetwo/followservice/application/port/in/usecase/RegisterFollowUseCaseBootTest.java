package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.RegisterFollowResponseDto;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.application.service.service.FollowService;
import com.onetwo.followservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.followservice.domain.Follow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RegisterFollowUseCaseBootTest {

    @Autowired
    private FollowService registerFollowUseCase;

    @Autowired
    private RegisterFollowPort registerFollowPort;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[통합][Use Case] Follow 등록 - 성공 테스트")
    void registerFollowUseCaseSuccessTest() {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);

        //when
        RegisterFollowResponseDto result = registerFollowUseCase.registerFollow(registerFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isRegisterSuccess());
    }

    @Test
    @DisplayName("[통합][Use Case] Follow 등록 follow 기등록 - 실패 테스트")
    void registerFollowUseCaseFollowAlreadyExistFailTest() {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);

        Follow savedFollow = Follow.createNewFollowByCommand(registerFollowCommand);

        registerFollowPort.registerFollow(savedFollow);

        //when then
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> registerFollowUseCase.registerFollow(registerFollowCommand));
    }
}