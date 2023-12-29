package com.claro.amx.sp.apigatewayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    DATABASE_EXCEPTION,
    TECHNICAL_EXCEPTION,
    BUSINESS_EXCEPTION,
    EXTERNAL_EXCEPTION,
    INTERNAL_EXCEPTION,
    CUSTOM_EXCEPTION,
    CONTROLLERS_EXCEPTION
}