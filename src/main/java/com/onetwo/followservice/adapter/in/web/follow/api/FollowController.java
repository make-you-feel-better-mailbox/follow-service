package com.onetwo.followservice.adapter.in.web.follow.api;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final RegisterFollowUseCase registerFollowUseCase;
    private final DeleteFollowUseCase deleteFollowUseCase;
    private final ReadFollowUseCase readFollowUseCase;
    public final FollowDtoMapper followDtoMapper;

    /**
     * Register Follow inbound adapter
     *
     * @param registerFollowRequest data about register follow
     * @param userId                user authentication id
     * @return Boolean about register success
     */
    @PostMapping(GlobalUrl.FOLLOW_ROOT)
    public ResponseEntity<RegisterFollowResponse> registerFollow(@RequestBody @Valid RegisterFollowRequest registerFollowRequest,
                                                                 @AuthenticationPrincipal String userId) {
        RegisterFollowCommand registerFollowCommand = followDtoMapper.registerRequestToCommand(userId, registerFollowRequest);
        RegisterFollowResponseDto registerFollowResponseDto = registerFollowUseCase.registerFollow(registerFollowCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(followDtoMapper.dtoToRegisterResponse(registerFollowResponseDto));
    }

    /**
     * Delete Follow inbound adapter
     *
     * @param targetUserId request delete follow target user id
     * @param userId       user authentication id
     * @return Boolean about delete follow success
     */
    @DeleteMapping(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE)
    public ResponseEntity<DeleteFollowResponse> deleteFollow(@PathVariable(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID) String targetUserId,
                                                             @AuthenticationPrincipal String userId) {
        DeleteFollowCommand deleteFollowCommand = followDtoMapper.deleteRequestToCommand(userId, targetUserId);
        DeleteFollowResponseDto deleteFollowResponseDto = deleteFollowUseCase.deleteFollow(deleteFollowCommand);
        return ResponseEntity.ok().body(followDtoMapper.dtoToDeleteResponse(deleteFollowResponseDto));
    }

    /**
     * Get Target's Follow count inbound adapter
     *
     * @param targetUserId request count Follow target user id
     * @return About target's Follow count
     */
    @GetMapping(GlobalUrl.FOLLOW_COUNT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE)
    public ResponseEntity<CountFollowResponse> getFollowCount(@PathVariable(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID) String targetUserId) {
        CountFollowCommand countFollowCommand = followDtoMapper.countRequestToCommand(targetUserId);
        CountFollowResponseDto countFollowResponseDto = readFollowUseCase.getFollowCount(countFollowCommand);
        return ResponseEntity.ok().body(followDtoMapper.dtoToCountResponse(countFollowResponseDto));
    }

    /**
     * Get Boolean about User Follow Target inbound adapter
     *
     * @param targetUserId request target user id
     * @param userId       user authentication id
     * @return Boolean about user Follow target
     */
    @GetMapping(GlobalUrl.FOLLOW_ROOT + GlobalUrl.PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE)
    public ResponseEntity<FollowTargetCheckResponse> userFollowTargetUserCheck(@PathVariable(GlobalUrl.PATH_VARIABLE_TARGET_USER_ID) String targetUserId,
                                                                               @AuthenticationPrincipal String userId) {
        FollowTargetCheckCommand followTargetCheckCommand = followDtoMapper.followCheckRequestToCommand(userId, targetUserId);
        FollowTargetCheckResponseDto followTargetCheckResponseDto = readFollowUseCase.userFollowTargetUserCheck(followTargetCheckCommand);
        return ResponseEntity.ok().body(followDtoMapper.dtoToFollowTargetCheckResponse(followTargetCheckResponseDto));
    }
}
