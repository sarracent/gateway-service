package com.claro.amx.sp.apigatewayservice.exception.impl;

import com.claro.amx.sp.apigatewayservice.exception.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.claro.amx.sp.apigatewayservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessExceptionTest {
    private BusinessException businessException;

    @BeforeEach
    void setUp() {
        businessException = new BusinessException("1122", "Business Exception Test", "ERROR", "Message");
    }

    @Test
    void getDescriptionTest_Succes() {
        String reason = "Business Exception Test";
        String code = "0000";
        String message = "Business Exception";
        try {
            throw new BusinessException(reason, code, null, message);
        } catch (BusinessException e) {
            assertEquals(reason, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String reason = "Business Exception Test";
        String code = "0000";
        String level = "ERROR";
        String message = "Business Exception";
        try {
            throw new BusinessException(reason, code, level, message);
        } catch (BusinessException e) {
            assertEquals(reason, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = businessException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = businessException.getExtraInfo();
        assertNotNull(list);
    }

}