package com.onetwo.followservice.adapter.in.web.follow.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.followservice.adapter.in.web.config.TestConfig;
import com.onetwo.followservice.adapter.in.web.follow.mapper.FollowFilterDtoMapper;
import com.onetwo.followservice.adapter.in.web.follow.request.FilterFollowSliceRequest;
import com.onetwo.followservice.adapter.in.web.follow.response.FilteredFollowResponse;
import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;
import com.onetwo.followservice.application.port.in.response.FilteredFollowResponseDto;
import com.onetwo.followservice.application.port.in.usecase.ReadFollowUseCase;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowFilterController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class FollowFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReadFollowUseCase readFollowUseCase;

    @MockBean
    private FollowFilterDtoMapper followFilterDtoMapper;

    private final String follower = "testUserId";
    private final String followerPath = "follower";
    private final String followee = "targetUserId";
    private final String followeePath = "followee";
    private final String pageNumber = "pageNumber";
    private final String pageSize = "pageSize";
    private final PageRequest pageRequest = PageRequest.of(0, 20);

    @Test
    @DisplayName("[단위][Web Adapter] Follow Filter 조회 성공 - 성공 테스트")
    void getFilteredFollowSuccessTest() throws Exception {
        //given
        FollowFilterCommand followFilterCommand = new FollowFilterCommand(pageRequest, follower, followee);

        List<FilteredFollowResponseDto> filteredFollowResponseDtoList = new ArrayList<>();

        FilteredFollowResponseDto testFilteredFollow = new FilteredFollowResponseDto(1L, follower, followee, Instant.now());
        filteredFollowResponseDtoList.add(testFilteredFollow);

        Slice<FilteredFollowResponseDto> filteredPostingResponseDtoSlice = new SliceImpl<>(filteredFollowResponseDtoList, pageRequest, true);

        List<FilteredFollowResponse> filteredFollowResponseList = filteredFollowResponseDtoList.stream()
                .map(responseDto -> new FilteredFollowResponse(
                        responseDto.followId(),
                        responseDto.follower(),
                        responseDto.followee(),
                        responseDto.createdDate()
                )).toList();

        Slice<FilteredFollowResponse> filteredFollowResponseSlice = new SliceImpl<>(filteredFollowResponseList, pageRequest, true);

        when(followFilterDtoMapper.filterRequestToCommand(any(FilterFollowSliceRequest.class))).thenReturn(followFilterCommand);
        when(readFollowUseCase.filterFollow(any(FollowFilterCommand.class))).thenReturn(filteredPostingResponseDtoSlice);
        when(followFilterDtoMapper.dtoToFilteredFollowResponse(any(Slice.class))).thenReturn(filteredFollowResponseSlice);

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
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[단위][Web Adapter] Follow Filter follower Null 조회 성공 - 성공 테스트")
    void getFilteredFollowerNullSuccessTest() throws Exception {
        //given
        FollowFilterCommand followFilterCommand = new FollowFilterCommand(pageRequest, null, followee);

        List<FilteredFollowResponseDto> filteredFollowResponseDtoList = new ArrayList<>();

        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            FilteredFollowResponseDto testFilteredFollow = new FilteredFollowResponseDto(i, follower + i, followee, Instant.now());
            filteredFollowResponseDtoList.add(testFilteredFollow);
        }

        Slice<FilteredFollowResponseDto> filteredPostingResponseDtoSlice = new SliceImpl<>(filteredFollowResponseDtoList, pageRequest, true);

        List<FilteredFollowResponse> filteredFollowResponseList = filteredFollowResponseDtoList.stream()
                .map(responseDto -> new FilteredFollowResponse(
                        responseDto.followId(),
                        responseDto.follower(),
                        responseDto.followee(),
                        responseDto.createdDate()
                )).toList();

        Slice<FilteredFollowResponse> filteredFollowResponseSlice = new SliceImpl<>(filteredFollowResponseList, pageRequest, true);

        when(followFilterDtoMapper.filterRequestToCommand(any(FilterFollowSliceRequest.class))).thenReturn(followFilterCommand);
        when(readFollowUseCase.filterFollow(any(FollowFilterCommand.class))).thenReturn(filteredPostingResponseDtoSlice);
        when(followFilterDtoMapper.dtoToFilteredFollowResponse(any(Slice.class))).thenReturn(filteredFollowResponseSlice);

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
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[단위][Web Adapter] Follow Filter Followee Null 조회 성공 - 성공 테스트")
    void getFilteredFolloweeNullUserIdSuccessTest() throws Exception {
        //given
        FollowFilterCommand followFilterCommand = new FollowFilterCommand(pageRequest, null, followee);

        List<FilteredFollowResponseDto> filteredFollowResponseDtoList = new ArrayList<>();

        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            FilteredFollowResponseDto testFilteredFollow = new FilteredFollowResponseDto(i, follower, followee + i, Instant.now());
            filteredFollowResponseDtoList.add(testFilteredFollow);
        }

        Slice<FilteredFollowResponseDto> filteredPostingResponseDtoSlice = new SliceImpl<>(filteredFollowResponseDtoList, pageRequest, true);

        List<FilteredFollowResponse> filteredFollowResponseList = filteredFollowResponseDtoList.stream()
                .map(responseDto -> new FilteredFollowResponse(
                        responseDto.followId(),
                        responseDto.follower(),
                        responseDto.followee(),
                        responseDto.createdDate()
                )).toList();

        Slice<FilteredFollowResponse> filteredFollowResponseSlice = new SliceImpl<>(filteredFollowResponseList, pageRequest, true);

        when(followFilterDtoMapper.filterRequestToCommand(any(FilterFollowSliceRequest.class))).thenReturn(followFilterCommand);
        when(readFollowUseCase.filterFollow(any(FollowFilterCommand.class))).thenReturn(filteredPostingResponseDtoSlice);
        when(followFilterDtoMapper.dtoToFilteredFollowResponse(any(Slice.class))).thenReturn(filteredFollowResponseSlice);

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .queryParam(follower, follower)
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.FOLLOW_FILTER + queryString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}