package com.onetwo.followservice.adapter.in.web.follow.mapper;

import com.onetwo.followservice.adapter.in.web.follow.request.FilterFollowSliceRequest;
import com.onetwo.followservice.adapter.in.web.follow.response.FilteredFollowResponse;
import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;
import com.onetwo.followservice.application.port.in.response.FilteredFollowResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowFilterDtoMapper {
    public FollowFilterCommand filterRequestToCommand(FilterFollowSliceRequest filterFollowSliceRequest) {
        Pageable pageable = PageRequest.of(
                filterFollowSliceRequest.pageNumber() == null ? 0 : filterFollowSliceRequest.pageNumber(),
                filterFollowSliceRequest.pageSize() == null ? 10 : filterFollowSliceRequest.pageSize()
        );

        return new FollowFilterCommand(pageable, filterFollowSliceRequest.follower(), filterFollowSliceRequest.followee());
    }

    public Slice<FilteredFollowResponse> dtoToFilteredFollowResponse(Slice<FilteredFollowResponseDto> filteredFollowResponseDto) {
        List<FilteredFollowResponse> filteredFollowResponseList = filteredFollowResponseDto.getContent().stream()
                .map(response -> new FilteredFollowResponse(
                        response.followId(),
                        response.follower(),
                        response.followee(),
                        response.createdDate()
                )).toList();

        return new SliceImpl<>(filteredFollowResponseList, filteredFollowResponseDto.getPageable(), filteredFollowResponseDto.hasNext());
    }
}
