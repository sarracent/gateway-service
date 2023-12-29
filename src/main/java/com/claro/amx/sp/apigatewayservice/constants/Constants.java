package com.claro.amx.sp.apigatewayservice.constants;

public class Constants {
    public static final String SERVICE = "Api Gateway";
    public static final String PASS_WORD = "Password";

    public static final String SESSION_NAME = "Session-Id";
    public static final String SESSION_DESCR = "Session-Id sent by client. It is the field that is used to keep track of the request made in the application.";

    public static final String CHANNEL_NAME = "Channel-Id";
    public static final String CHANNEL_DESCR = "Channel-Id sent by client. It is the field that is used to indicate the channel that the application consumes.";

    public static final String USER_NAME = "User-Id";

    public static final String BEARER_TOKEN = "authorization";
    public static final String BEARER_TOKEN_DESCR = "Bearer Token for Authorization";

    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MSG = "Success";
    public static final String BADREQUEST_CODE = "400";
    public static final String BADREQUEST_MSG = "Bad Request";
    public static final String INTERNALSERVER_CODE = "500";
    public static final String INTERNALSERVER_MSG = "Internal Server Error";

    public static final String OK_MSG = "OK";
    public static final String ZERO_MSG = "0";
    public static final String Y = "Y";
    public static final String N = "N";
    public static final String OPNAME = "%s.%s";
    public static final String UNDERSCORE = "_";
    public static final String PARSE_EXCEPTION = "%s - Line Code Error: %s";
    public static final String ERROR_LEVEL = "ERROR";
    public static final String SERVICE_MESSAGES_CACHE="pre:service_messages:1";
    public static final long TIME_TO_LIVE = 300;

    private Constants() {
    }
}
