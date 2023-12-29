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
class ControllersExceptionTest {

    private ControllersException controllersException;

    @BeforeEach
    void setUp() {
        controllersException = new ControllersException("0000", "Controller Exception Test", "ERROR");
    }

    @Test
    void getDescription() {
        String message = "Controller Exception Test";
        String code = "0000";
        String service = "sp-api-gateway-service";
        try {
            throw new ControllersException(message, code, null);
        } catch (ControllersException e) {
            assertEquals(message, e.getReason());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String message = "Controller Exception Test";
        String code = "0000";
        String level = "ERROR";
        try {
            throw new ControllersException(message, code, level);
        } catch (ControllersException e) {
            assertEquals(level, e.getLevel());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = controllersException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String error = controllersException.getReason();
        assertEquals("0000", error);
    }

    @Test
    void getMessage() {
        final String message = controllersException.getCode();
        assertEquals("Controller Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = controllersException.getExtraInfo();
        assertNotNull(list);
    }

}