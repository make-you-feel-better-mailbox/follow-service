package com.onetwo.followservice.application.port.in.usecase;

import com.onetwo.followservice.application.port.in.command.CountFollowCommand;
import com.onetwo.followservice.application.port.in.command.FollowTargetCheckCommand;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.response.CountFollowResponseDto;
import com.onetwo.followservice.application.port.in.response.FollowTargetCheckResponseDto;
import com.onetwo.followservice.application.port.out.ReadFollowPort;
import com.onetwo.followservice.application.service.converter.FollowUseCaseConverter;
import com.onetwo.followservice.application.service.service.FollowService;
import com.onetwo.followservice.domain.Follow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReadFollowUseCaseTest {

    @InjectMocks
    private FollowService readFollowUseCase;

    @Mock
    private ReadFollowPort readFollowPort;

    @Mock
    private FollowUseCaseConverter followUseCaseConverter;

    private final String follower = "testUserId";
    private final String followee = "targetUserId";
    private final long followerCount = 12L;
    private final long followeeCount = 7L;

    @Test
    @DisplayName("[단위][Use Case] Follow 갯수 조회 - 성공 테스트")
    void countFollowUseCaseSuccessTest() {
        //given
        CountFollowCommand CountFollowCommand = new CountFollowCommand(followee);
        CountFollowResponseDto CountFollowResponseDto = new CountFollowResponseDto(followerCount, followeeCount);

        given(readFollowPort.countFollowByFollowee(anyString())).willReturn(followerCount);
        given(readFollowPort.countFollowByFollower(anyString())).willReturn(followeeCount);
        given(followUseCaseConverter.resultToCountResponseDto(anyLong(), anyLong())).willReturn(CountFollowResponseDto);
        //when
        CountFollowResponseDto result = readFollowUseCase.getFollowCount(CountFollowCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(followerCount, result.followerCount());
        Assertions.assertEquals(followeeCount, result.followeeCount());
    }

    @Test
    @DisplayName("[단위][Use Case] Follow 등록 여부 조회 - 성공 테스트")
    void userTargetFollowCheckUseCaseSuccessTest() {
        //given
        FollowTargetCheckCommand followTargetCheckCommand = new FollowTargetCheckCommand(follower, followee);
        FollowTargetCheckResponseDto followTargetCheckResponseDto = new FollowTargetCheckResponseDto(true);

        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);
        Follow follow = Follow.createNewFollowByCommand(registerFollowCommand);

        given(readFollowPort.findByFollowerAndFollowee(anyString(), anyString())).willReturn(Optional.of(follow));
        given(followUseCaseConverter.resultToFollowTargetCheckResponseDto(anyBoolean())).willReturn(followTargetCheckResponseDto);
        //when
        FollowTargetCheckResponseDto result = readFollowUseCase.userFollowTargetUserCheck(followTargetCheckCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isUserFollowTargetUser());
    }
}