package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.RegisterFollowResponseDto;
import com.onetwo.followservice.application.port.out.ReadFollowPort;
import com.onetwo.followservice.application.port.out.RegisterFollowPort;
import com.onetwo.followservice.application.service.converter.FollowUseCaseConverter;
import com.onetwo.followservice.application.service.service.FollowService;
import com.onetwo.followservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.followservice.domain.Follow;
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
class RegisterFollowUseCaseTest {

    @InjectMocks
    private FollowService registerFollowUseCase;

    @Mock
    private ReadFollowPort readFollowPort;

    @Mock
    private RegisterFollowPort registerFollowPort;

    @Mock
    private FollowUseCaseConverter followUseCaseConverter;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[단위][Use Case] Follow 등록 - 성공 테스트")
    void registerFollowUseCaseSuccessTest() {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);

        RegisterFollowResponseDto registerFollowResponseDto = new RegisterFollowResponseDto(true);

        Follow savedFollow = Follow.createNewFollowByCommand(registerFollowCommand);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.empty());
        given(registerFollowPort.registerFollow(any(Follow.class))).willReturn(savedFollow);
        given(followUseCaseConverter.followToRegisterResponseDto(any(Follow.class))).willReturn(registerFollowResponseDto);
        //when
        RegisterFollowResponseDto result = registerFollowUseCase.registerFollow(registerFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isRegisterSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] Follow 등록 follow 기등록 - 실패 테스트")
    void registerFollowUseCaseFollowAlreadyExistFailTest() {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);

        Follow savedFollow = Follow.createNewFollowByCommand(registerFollowCommand);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.of(savedFollow));
        //when then
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> registerFollowUseCase.registerFollow(registerFollowCommand));
    }
}