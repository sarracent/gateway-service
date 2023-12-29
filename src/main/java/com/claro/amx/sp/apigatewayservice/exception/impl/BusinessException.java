package com.claro.amx.sp.apigatewayservice.exception.impl;

import com.claro.amx.sp.apigatewayservice.exception.CustomException;

public class BusinessException extends RuntimeException implements CustomException {

    private static final long serialVersionUID = -1132348466005485433L;
    private final String reason;
    private final String code;
    private final String level;
    private final String message;

    public BusinessException(String reason, String code, String level, String message) {
        super(reason);
        this.code = code;
        this.reason = reason;
        this.level = level;
        this.message = message;
    }


    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLevel(){ return level; }

    @Override
    public String getMessage(){ return message; }

}