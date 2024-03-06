package com.onetwo.followservice.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalUrl {

    public static final String ROOT_URI = "/follow-service";
    public static final String FOLLOW_ROOT = ROOT_URI + "/follows";
    public static final String UNDER_ROUTE = "/*";
    public static final String EVERY_UNDER_ROUTE = "/**";

    public static final String PATH_VARIABLE_TARGET_USER_ID = "target-user-id";
    public static final String PATH_VARIABLE_TARGET_USER_ID_WITH_BRACE = "/{" + PATH_VARIABLE_TARGET_USER_ID + "}";

    public static final String FOLLOW_COUNT = FOLLOW_ROOT + "/counts";
}
