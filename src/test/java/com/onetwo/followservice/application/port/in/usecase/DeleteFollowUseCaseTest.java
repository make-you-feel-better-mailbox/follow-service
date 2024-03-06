package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.DeleteFollowCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.DeleteFollowResponseDto;
import com.onetwo.followservice.application.port.out.ReadFollowPort;
import com.onetwo.followservice.application.port.out.UpdateFollowPort;
import com.onetwo.followservice.application.service.converter.FollowUseCaseConverter;
import com.onetwo.followservice.application.service.service.FollowService;
import com.onetwo.followservice.domain.Follow;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteFollowUseCaseTest {

    @InjectMocks
    private FollowService deleteFollowUseCase;

    @Mock
    private ReadFollowPort readFollowPort;

    @Mock
    private UpdateFollowPort updateFollowPort;

    @Mock
    private FollowUseCaseConverter followUseCaseConverter;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[단위][Use Case] Follow 삭제 - 성공 테스트")
    void deleteFollowUseCaseSuccessTest() {
        //given
        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);
        DeleteFollowResponseDto deleteFollowResponseDto = new DeleteFollowResponseDto(true);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.of(follow));
        given(followUseCaseConverter.followToDeleteResponseDto(any(Follow.class))).willReturn(deleteFollowResponseDto);
        //when
        DeleteFollowResponseDto result = deleteFollowUseCase.deleteFollow(deleteFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isDeleteSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] Follow 삭제 follow does not exist - 실패 테스트")
    void deleteFollowUseCaseFollowDoesNotExistFailTest() {
        //given
        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deleteFollowUseCase.deleteFollow(deleteFollowCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Follow 삭제 user id does not matched - 실패 테스트")
    void deleteFollowUseCaseUserIdDoesNotMatchedFailTest() {
        //given
        String wrongUserId = "wrongUserId";

        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(follower, followee);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(wrongUserId, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.of(follow));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> deleteFollowUseCase.deleteFollow(deleteFollowCommand));
    }
}