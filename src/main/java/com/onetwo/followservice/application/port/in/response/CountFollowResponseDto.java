package com.onetwo.followservice.application.port.in.response;

public record CountFollowResponseDto(long followerCount,
                                     long followeeCount) {
}
