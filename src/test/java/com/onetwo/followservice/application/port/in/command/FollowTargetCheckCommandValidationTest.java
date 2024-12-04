package com.onetwo.followservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class FollowTargetCheckCommandValidationTest {

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[단위][Command Validation] Follow Target Check Command Validation test - 성공 테스트")
    void likeTargetCheckCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new FollowTargetCheckCommand(follower, followee));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Follow Target Check Command follower Validation fail test - 실패 테스트")
    void likeTargetCheckCommandFollowerValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new FollowTargetCheckCommand(testUserId, followee));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Follow Target Check Command followee Validation fail test - 실패 테스트")
    void likeTargetCheckCommandFolloweeValidationFailTest(String testTargetUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new FollowTargetCheckCommand(follower, testTargetUserId));
    }
}