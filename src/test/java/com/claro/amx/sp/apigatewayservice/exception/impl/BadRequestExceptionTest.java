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
class BadRequestExceptionTest {

    private BadRequestException badRequestException;

    @BeforeEach
    void setUp() {
        badRequestException = new BadRequestException("0000", "Bad Request Exception Test", "ERROR", "Message");
    }

    @Test
    void getDescription() {
        String reason = "Bad Request Exception Test";
        String code = "0000";
        String message = "Message";
        try {
            throw new BadRequestException(reason, code, null, message);
        } catch (BadRequestException e) {
            assertEquals(reason, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String reason = "Bad Request Exception Test";
        String code = "0000";
        String level = "ERROR";
        String message = "Bad Request";
        try {
            throw new BadRequestException(reason, code, level, message);
        } catch (BadRequestException e) {
            assertEquals(level, e.getLevel());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = badRequestException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String error = badRequestException.getReason();
        assertEquals("0000", error);
    }

    @Test
    void getMessage() {
        final String message = badRequestException.getCode();
        assertEquals("Bad Request Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = badRequestException.getExtraInfo();
        assertNotNull(list);
    }
}