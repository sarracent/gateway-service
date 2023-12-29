package com.claro.amx.sp.apigatewayservice.exception.impl;

import com.claro.amx.sp.apigatewayservice.exception.CustomException;
import com.claro.amx.sp.apigatewayservice.exception.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.claro.amx.sp.apigatewayservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomExceptionTest {
    private CustomException customException;

    @BeforeEach
    void setUp() {
        customException = new CustomException() {
            @Override
            public String getReason() {
                return "Custom Exception Test";
            }

            @Override
            public String getCode() {
                return "0000";
            }

            @Override
            public String getLevel() {
                return "Error";
            }

            @Override
            public String getMessage() {
                return "Message";
            }
        };
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = customException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = customException.getExtraInfo();
        assertNotNull(list);
    }
}