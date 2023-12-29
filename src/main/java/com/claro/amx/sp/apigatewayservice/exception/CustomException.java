package com.claro.amx.sp.apigatewayservice.exception;

import java.util.Collections;
import java.util.List;

public interface CustomException {
    String getReason();

    String getCode();

    String getLevel();

    String getMessage();

    default ExceptionType getExceptionType() {
        return ExceptionType.CUSTOM_EXCEPTION;
    }

    default List<Object> getExtraInfo() {
        return Collections.emptyList();
    }

}
