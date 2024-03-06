package com.onetwo.followservice.adapter.in.web.follow.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.followservice.adapter.in.web.config.TestConfig;
import com.onetwo.followservice.adapter.in.web.follow.mapper.FollowDtoMapper;
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
import com.onetwo.followservice.application.port.in.usecase.DeleteFollowUseCase;
import com.onetwo.followservice.application.port.in.usecase.ReadFollowUseCase;
import com.onetwo.followservice.application.port.in.usecase.RegisterFollowUseCase;
import com.onetwo.followservice.common.GlobalUrl;
import com.onetwo.followservice.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterFollowUseCase registerFollowUseCase;

    @MockBean
    private DeleteFollowUseCase deleteFollowUseCase;

    @MockBean
    private ReadFollowUseCase readFollowUseCase;

    @MockBean
    private FollowDtoMapper followDtoMapper;

    private final String userId = "testUserId";

    private final String targetUserId = "targetUserId";

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Follow 등록 - 성공 테스트")
    void registerFollowSuccessTest() throws Exception {
        //given
        RegisterFollowRequest registerFollowRequest = new RegisterFollowRequest(targetUserId);
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(userId, registerFollowRequest.targetUserId());
        RegisterFollowResponseDto registerFollowResponseDto = new RegisterFollowResponseDto(true);
        RegisterFollowResponse registerFollowResponse = new RegisterFollowResponse(true);

        when(followDtoMapper.registerRequestToCommand(anyString(), any(RegisterFollowRequest.class))).thenReturn(registerFollowCommand);
        when(registerFollowUseCase.registerFollow(any(RegisterFollowCommand.class))).thenReturn(registerFollowResponseDto);
        when(followDtoMapper.dtoToRegisterResponse(any(RegisterFollowResponseDto.class))).thenReturn(registerFollowResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.FOLLOW_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFollowRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Follow 삭제 - 성공 테스트")
    void deleteFollowSuccessTest() throws Exception {
        //given
        DeleteFollowCommand deleteFollowCommand = new DeleteFollowCommand(userId, targetUserId);
        DeleteFollowResponseDto deleteFollowResponseDto = new DeleteFollowResponseDto(true);
        DeleteFollowResponse deletePostingCommand = new DeleteFollowResponse(true);

        when(followDtoMapper.deleteRequestToCommand(anyString(), anyString())).thenReturn(deleteFollowCommand);
        when(deleteFollowUseCase.deleteFollow(any(DeleteFollowCommand.class))).thenReturn(deleteFollowResponseDto);
        when(followDtoMapper.dtoToDeleteResponse(any(DeleteFollowResponseDto.class))).thenReturn(deletePostingCommand);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[단위][Web Adapter] Follow 갯수 조회 - 성공 테스트")
    void countFollowSuccessTest() throws Exception {
        //given
        CountFollowCommand countFollowCommand = new CountFollowCommand(targetUserId);
        CountFollowResponseDto countFollowResponseDto = new CountFollowResponseDto(1016, 435);
        CountFollowResponse countFollowResponse = new CountFollowResponse(countFollowResponseDto.followerCount(), countFollowResponseDto.followeeCount());

        when(followDtoMapper.countRequestToCommand(anyString())).thenReturn(countFollowCommand);
        when(readFollowUseCase.getFollowCount(any(CountFollowCommand.class))).thenReturn(countFollowResponseDto);
        when(followDtoMapper.dtoToCountResponse(any(CountFollowResponseDto.class))).thenReturn(countFollowResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_COUNT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Follow 등록 여부 조회 - 성공 테스트")
    void userTargetFollowCheckSuccessTest() throws Exception {
        //given
        FollowTargetCheckCommand FollowTargetCheckCommand = new FollowTargetCheckCommand(userId, targetUserId);
        FollowTargetCheckResponseDto FollowTargetCheckResponseDto = new FollowTargetCheckResponseDto(true);
        FollowTargetCheckResponse FollowTargetCheckResponse = new FollowTargetCheckResponse(FollowTargetCheckResponseDto.isUserFollowTargetUser());

        when(followDtoMapper.followCheckRequestToCommand(anyString(), anyString())).thenReturn(FollowTargetCheckCommand);
        when(readFollowUseCase.userFollowTargetUserCheck(any(FollowTargetCheckCommand.class))).thenReturn(FollowTargetCheckResponseDto);
        when(followDtoMapper.dtoToFollowTargetCheckResponse(any(FollowTargetCheckResponseDto.class))).thenReturn(FollowTargetCheckResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}