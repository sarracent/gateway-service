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
class TechnicalExceptionTest {
    private TechnicalException technicalException;

    @BeforeEach
    void setUp() {
        technicalException = new TechnicalException("0000", "Technical Exception Test", "ERROR");
    }

    @Test
    void getDescription() {
        String message = "Technical Exception Test";
        String code = "0000";
        String service = "sp-api-gateway-service";
        try {
            throw new TechnicalException(message, code, null);
        } catch (TechnicalException e) {
            assertEquals(message, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String message = "Technical Exception Test";
        String code = "0000";
        String level = "ERROR";
        try {
            throw new TechnicalException(message, code, level);
        } catch (TechnicalException e) {
            assertEquals(level, e.getLevel());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = technicalException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String error = technicalException.getReason();
        assertEquals("0000", error);
    }

    @Test
    void getMessage() {
        final String message = technicalException.getCode();
        assertEquals("Technical Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = technicalException.getExtraInfo();
        assertNotNull(list);
    }
}