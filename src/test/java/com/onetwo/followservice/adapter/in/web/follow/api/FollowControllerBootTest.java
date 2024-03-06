package com.onetwo.followservice.adapter.in.web.follow.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.followservice.adapter.in.web.config.TestHeader;
import com.onetwo.followservice.adapter.in.web.follow.request.RegisterFollowRequest;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.usecase.RegisterFollowUseCase;
import com.onetwo.followservice.common.GlobalUrl;
import onetwo.mailboxcommonconfig.common.GlobalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader.class)
class FollowControllerBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterFollowUseCase registerFollowUseCase;

    @Autowired
    private TestHeader testHeader;

    private final String userId = "testUserId";
    private final String targetUserId = "targetUserId";
    private final long followerCount = 12L;
    private final long followeeCount = 7L;

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow 등록 - 성공 테스트")
    void registerFollowSuccessTest() throws Exception {
        //given
        RegisterFollowRequest registerFollowRequest = new RegisterFollowRequest(targetUserId);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.FOLLOW_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFollowRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("register-follow",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("targetUserId").type(JsonFieldType.STRING).description("Follow를 등록할 target user의 id")
                                ),
                                responseFields(
                                        fieldWithPath("isRegisterSuccess").type(JsonFieldType.BOOLEAN).description("등록 완료 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow 삭제 - 성공 테스트")
    void deleteFollowSuccessTest() throws Exception {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(userId, targetUserId);

        registerFollowUseCase.registerFollow(registerFollowCommand);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-follow",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                pathParameters(
                                        parameterWithName(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID).description("삭제할 Followee의 user id")
                                ),
                                responseFields(
                                        fieldWithPath("isDeleteSuccess").type(JsonFieldType.BOOLEAN).description("삭제 성공 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow 갯수 조회 - 성공 테스트")
    void countFollowSuccessTest() throws Exception {
        //given
        for (int i = 0; i < followerCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(userId + i, targetUserId);
            registerFollowUseCase.registerFollow(registerFollowCommand);
        }
        for (int i = 0; i < followeeCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(targetUserId, userId + i);
            registerFollowUseCase.registerFollow(registerFollowCommand);
        }

        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_COUNT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("count-follow",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key")
                                ),
                                pathParameters(
                                        parameterWithName(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID).description("조회할 user의 user id")
                                ),
                                responseFields(
                                        fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("Follower 수"),
                                        fieldWithPath("followeeCount").type(JsonFieldType.NUMBER).description("Followee 수")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @WithMockUser(username = userId)
    @DisplayName("[통합][Web Adapter] Follow 등록 여부 조회 - 성공 테스트")
    void userTargetFollowCheckSuccessTest() throws Exception {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(userId, targetUserId);
        registerFollowUseCase.registerFollow(registerFollowCommand);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE
                        , targetUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-follow-check",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                pathParameters(
                                        parameterWithName(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID).description("조회할 Followee의 target user id")
                                ),
                                responseFields(
                                        fieldWithPath("isUserFollowTargetUser").type(JsonFieldType.BOOLEAN).description("Follow 등록 여부")
                                )
                        )
                );
    }
}