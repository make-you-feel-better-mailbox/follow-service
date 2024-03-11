package com.onetwo.followservice.adapter.in.web.follow.api;

import com.onetwo.followservice.adapter.in.web.follow.mapper.FollowFilterDtoMapper;
import com.onetwo.followservice.adapter.in.web.follow.request.FilterFollowSliceRequest;
import com.onetwo.followservice.adapter.in.web.follow.response.FilteredFollowResponse;
import com.onetwo.followservice.application.port.in.command.FollowFilterCommand;
import com.onetwo.followservice.application.port.in.response.FilteredFollowResponseDto;
import com.onetwo.followservice.application.port.in.usecase.ReadFollowUseCase;
import com.onetwo.followservice.common.GlobalUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowFilterController {

    private final ReadFollowUseCase readFollowUseCase;
    private final FollowFilterDtoMapper followFilterDtoMapper;

    /**
     * Get Filtered follow inbound adapter
     *
     * @param filterFollowSliceRequest filter condition and pageable
     * @return content and slice data
     */
    @GetMapping(GlobalUrl.FOLLOW_FILTER)
    public ResponseEntity<Slice<FilteredFollowResponse>> filterFollow(@ModelAttribute FilterFollowSliceRequest filterFollowSliceRequest) {
        FollowFilterCommand followFilterCommand = followFilterDtoMapper.filterRequestToCommand(filterFollowSliceRequest);
        Slice<FilteredFollowResponseDto> filteredFollowResponseDto = readFollowUseCase.filterFollow(followFilterCommand);
        return ResponseEntity.ok().body(followFilterDtoMapper.dtoToFilteredFollowResponse(filteredFollowResponseDto));
    }
}
