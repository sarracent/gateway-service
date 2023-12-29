package com.claro.amx.sp.apigatewayservice.exception.impl;


import com.claro.amx.sp.apigatewayservice.exception.CustomException;

public class ControllersException extends RuntimeException implements CustomException {

    private static final long serialVersionUID = -1132348466005485433L;
    private final String reason;
    private final String code;
    private final String level;

    public ControllersException(String reason, String code, String level) {
        super(reason);
        this.reason = reason;
        this.code = code;
        this.level = level;
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
    public String getLevel(){return level;}

}