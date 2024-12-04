package com.onetwo.followservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class CountFollowCommandValidationTest {

    private final String targetUserId = "targetUserId";

    @Test
    @DisplayName("[단위][Command Validation] Count Follow Command Validation test - 성공 테스트")
    void countFollowCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new CountFollowCommand(targetUserId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Count Follow Command targetUserId Validation fail test - 실패 테스트")
    void countFollowCommandCategoryValidationFailTest(String testTargetUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new CountFollowCommand(testTargetUserId));
    }
}