package com.claro.amx.sp.apigatewayservice.commons.aop;

import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.utility.Util;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.CHANNEL_NAME;
import static com.claro.amx.sp.apigatewayservice.constants.Constants.SESSION_NAME;
import static com.claro.amx.sp.apigatewayservice.utility.Logs.Basic.OPERATION;
import static com.claro.amx.sp.apigatewayservice.utility.Logs.Basic.REQUEST;
import static com.claro.amx.sp.apigatewayservice.utility.Logs.Header.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class LogAspectTest {
    LogAspect logAspect;
    @Mock
    ProceedingJoinPoint proceedingJoinPoint;

    @BeforeEach
    public void setUp() {
        logAspect = new LogAspect();
    }


    @Test
    void logOperationTime() {
        Exception exception = assertThrows(Exception.class, () -> {
            logAspect.logOperationTime(null);
        });
        assertNotNull(exception.getMessage());
    }

    @Test()
    void afterAnyException() {
        Exception exception = new Exception();
        logAspect.afterAnyException(null, exception);
        assertNotNull(exception);
    }

    @Test()
    void afterCustomException() {
        BusinessException exception = new BusinessException("1122", "Business Exception Test", "ERROR", "Message");
        logAspect.afterAnyException(null, exception);
        assertNotNull(exception);
    }

    @Test
    void updateLogParameter_shouldUpdateThreadContext() {
        // Given
        String operationName = "operationName";
        String methodName = "methodName";
        Map<String, Object> parametersMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(SESSION_NAME, "sessionId");
        headersMap.put(CHANNEL_NAME, "channelId");

        // When
        logAspect.updateLogParameter(operationName, methodName, parametersMap, headersMap);
        // Then
        assertEquals(ThreadContext.get(TRANSACTION_ID.name()).length(), 43);
        assertEquals(ThreadContext.get(SESSION_ID.name()), "sessionId");
        assertEquals(ThreadContext.get(CHANNEL_ID.name()), "channelId");
        assertEquals(ThreadContext.get(OPERATION.name()), operationName);
        assertEquals(ThreadContext.get(REQUEST.name()), "{}"); // or change "{}" to the expected value of getRequest()
    }

    @Test
    void testGetResponse() {
        LogAspect logAspect = new LogAspect();

        // Test response object that is not an instance of ResponseEntity or Throwable
        Object response = new Object();
        String actualResponse = logAspect.getResponse(response);
        String expectedResponse = Util.toJSONString(response);
        assertEquals(expectedResponse, actualResponse);

        // Test response object that is an instance of ResponseEntity
        response = ResponseEntity.ok("response body");
        actualResponse = logAspect.getResponse(response);
        expectedResponse = Util.toJSONString(((ResponseEntity<?>) response).getBody());
        assertEquals(expectedResponse, actualResponse);

        // Test response object that is an instance of Throwable
        response = new Exception("exception message");
        actualResponse = logAspect.getResponse(response);
        expectedResponse = ((Throwable) response).getStackTrace()[0].toString();
        assertEquals(expectedResponse, actualResponse);
    }
}