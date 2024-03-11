package com.onetwo.followservice.adapter.in.web.follow.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.followservice.adapter.in.web.config.TestHeader;
import com.onetwo.followservice.application.port.in.command.RegisterFollowCommand;
import com.onetwo.followservice.application.port.in.usecase.RegisterFollowUseCase;
import com.onetwo.followservice.common.GlobalStatus;
import com.onetwo.followservice.common.GlobalUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader.class)
class FollowFilterControllerBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterFollowUseCase registerFollowUseCase;

    @Autowired
    private TestHeader testHeader;

    private final String follower = "testUserId";
    private final String followerPath = "follower";
    private final String followee = "targetUserId";
    private final String followeePath = "followee";
    private final String pageNumber = "pageNumber";
    private final String pageSize = "pageSize";
    private final PageRequest pageRequest = PageRequest.of(0, 20);
    private final int followCount = 23;

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow Filter 조회 성공 - 성공 테스트")
    void getFilteredFollowSuccessTest() throws Exception {
        //given
        RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee);
        registerFollowUseCase.registerFollow(registerFollowCommand);

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .queryParam(followerPath, follower)
                .queryParam(followeePath, followee)
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_FILTER + queryString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("filtering-follow",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key")
                                ),
                                queryParameters(
                                        parameterWithName(followerPath).description("조회할 follow의 follower"),
                                        parameterWithName(followeePath).description("조회할 follow의 followee"),
                                        parameterWithName(pageNumber).description("조회할 follow slice 페이지 번호"),
                                        parameterWithName(pageSize).description("조회할 follow slice size")
                                ),
                                responseFields(
                                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("follow List"),
                                        fieldWithPath("content[].followId").type(JsonFieldType.NUMBER).description("follow의 id"),
                                        fieldWithPath("content[].follower").type(JsonFieldType.STRING).description("follow의 follower"),
                                        fieldWithPath("content[].followee").type(JsonFieldType.STRING).description("follow의 followee"),
                                        fieldWithPath("content[].createdDate").type(JsonFieldType.STRING).description("follow 등록 일자"),
                                        fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable object"),
                                        fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("조회 페이지 번호"),
                                        fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("조회 한 size"),
                                        fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("sort object"),
                                        fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("sort 요청 여부"),
                                        fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("sort 여부"),
                                        fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsort 여부"),
                                        fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("대상 시작 번호"),
                                        fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("unpaged"),
                                        fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("paged"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("List 크기"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("조회 페이지 번호"),
                                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("sort object"),
                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("sort 요청 여부"),
                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("sort 여부"),
                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsort 여부"),
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("처음인지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막인지 여부"),
                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow Filter follower null 조회 성공 - 성공 테스트")
    void getFilteredFollowerNullSuccessTest() throws Exception {
        //given
        for (int i = 0; i <= followCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower + i, followee);
            registerFollowUseCase.registerFollow(registerFollowCommand);
        }

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .queryParam(followeePath, followee)
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_FILTER + queryString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Follow Filter followee null 조회 성공 - 성공 테스트")
    void getFilteredFollowNullFolloweeSuccessTest() throws Exception {
        //given
        for (int i = 0; i <= followCount; i++) {
            RegisterFollowCommand registerFollowCommand = new RegisterFollowCommand(follower, followee + i);
            registerFollowUseCase.registerFollow(registerFollowCommand);
        }

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .queryParam(followerPath, follower)
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_FILTER + queryString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}