package com.onetwo.followservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class DeleteFollowCommandValidationTest {

    private final String follower = "testUserId";
    private final String followee = "targetUserId";

    @Test
    @DisplayName("[단위][Command Validation] Delete Follow Command Validation test - 성공 테스트")
    void deleteFollowCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new DeleteFollowCommand(follower, followee));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete Follow Command follower Validation fail test - 실패 테스트")
    void deleteFollowCommandFollowerValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteFollowCommand(testUserId, followee));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete Follow Command followee Validation fail test - 실패 테스트")
    void deleteFollowCommandFolloweeValidationFailTest(String testTargetUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteFollowCommand(follower, testTargetUserId));
    }
}