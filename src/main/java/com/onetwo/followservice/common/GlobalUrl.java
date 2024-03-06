package com.onetwo.followservice.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalUrl {

    public static final String ROOT_URI = "/follow-service";
    public static final String UNDER_ROUTE = "/*";
    public static final String EVERY_UNDER_ROUTE = "/**";
}
