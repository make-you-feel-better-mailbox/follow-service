package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.DeleteFollowCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.DeleteFollowResponseDto;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.domain.Follow;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DeleteFollowUseCaseBootTest {

    @Autowired
    private DeleteFollowUseCase deleteFollowUseCase;

    @Autowired
    private RegisterFollowPort registerFollowPort;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[통합][Use Case] Follow 삭제 - 성공 테스트")
    void deleteFollowUseCaseSuccessTest() {
        //given
        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        registerFollowPort.registerFollow(follow);

        //when
        DeleteFollowResponseDto result = deleteFollowUseCase.deleteFollow(deleteFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isDeleteSuccess());
    }

    @Test
    @DisplayName("[통합][Use Case] Follow 삭제 follow does not exist - 실패 테스트")
    void deleteFollowUseCaseFollowDoesNotExistFailTest() {
        //given
        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deleteFollowUseCase.deleteFollow(deleteFollowCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Follow 삭제 follow does not exist - 실패 테스트")
    void deleteFollowUseCaseUserIdDoesNotMatchedFailTest() {
        //given
        String wrongUserId = "wrongUserId";

        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(wrongUserId, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        registerFollowPort.registerFollow(follow);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deleteFollowUseCase.deleteFollow(deleteFollowCommand));
    }
}