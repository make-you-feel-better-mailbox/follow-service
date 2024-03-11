package com.onetwo.followservice.adapter.in.web.follow.request;

public record FilterFollowSliceRequest(String follower,
                                       String followee,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       String sort) {
}
