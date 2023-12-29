package com.claro.amx.sp.apigatewayservice.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class LogUtilTest {

    private Logger mockLogger;

    @BeforeEach
    public void setUp() {
        mockLogger = Mockito.mock(Logger.class);
    }

    @Test
    void testLogFinishOperation() {
        // Mock
        Logger mockLogger = mock(Logger.class);

        // Call the method to test
        String code = "code";
        String description = "description";
        String elapsed = "elapsed";
        LogUtil.logFinishOperation(mockLogger, code, description, elapsed);

        // Verify the interaction with the logger
        verify(mockLogger).info(anyString());
    }

    @Test
    void testLogIntermediateOperation_DebugDisabled() {
        // Given
        when(mockLogger.isDebugEnabled()).thenReturn(false);

        String operation = "someOperation";
        String code = "200";
        String description = "OK";
        String elapsed = "100ms";
        String request = "requestInfo";
        String response = "responseInfo";

        // When
        LogUtil.logIntermediateOperation(mockLogger, operation, code, description, elapsed, request, response);

        // Then
        verify(mockLogger, never()).debug(anyString());
    }

    @Test
    void testLogIntermediateOperation_DebugEnable() {
        // Given
        when(mockLogger.isDebugEnabled()).thenReturn(true);

        String operation = "someOperation";
        String code = "200";
        String description = "OK";
        String elapsed = "100ms";
        String request = "requestInfo";
        String response = "responseInfo";

        // When
        LogUtil.logIntermediateOperation(mockLogger, operation, code, description, elapsed, request, response);

        // Then
        verify(mockLogger).debug(anyString());
    }
}
