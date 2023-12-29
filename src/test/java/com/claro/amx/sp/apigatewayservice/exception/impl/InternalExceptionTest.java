package com.claro.amx.sp.apigatewayservice.exception.impl;

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
class InternalExceptionTest {
    private InternalException internalException;

    @BeforeEach
    void setUp() {
        internalException = new InternalException("0000", "Internal Exception Test", "ERROR");
    }

    @Test
    void getDescription() {
        String message = "Internal Exception Test";
        String code = "0000";
        try {
            throw new InternalException(message, code, null);
        } catch (InternalException e) {
            assertEquals(message, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String message = "Internal Exception Test";
        String code = "0000";
        String level = "ERROR";
        try {
            throw new InternalException(message,code,level);
        } catch (InternalException e) {
            assertEquals(level, e.getLevel());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = internalException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String error = internalException.getReason();
        assertEquals("0000", error);
    }

    @Test
    void getMessage() {
        final String message = internalException.getCode();
        assertEquals("Internal Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = internalException.getExtraInfo();
        assertNotNull(list);
    }

}