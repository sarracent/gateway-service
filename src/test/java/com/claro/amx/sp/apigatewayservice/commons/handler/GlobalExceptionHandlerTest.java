package com.claro.amx.sp.apigatewayservice.commons.handler;

import com.claro.amx.sp.apigatewayservice.exception.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.ExpiredJwtException;




import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private ServerWebExchange exchange;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private MockServerHttpResponse response;

    @BeforeEach
    void setUp() {
        response = new MockServerHttpResponse();
        when(exchange.getResponse()).thenReturn(response);
    }

    @Test
    void handleBusinessException() {
        BusinessException businessException = new BusinessException("Error Code", "Error Level", "Error Reason", "Additional Info");
        Mono<Void> result = globalExceptionHandler.handle(exchange, businessException);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void handleBadRequestException() {
        BadRequestException badRequestException = new BadRequestException("Error Code", "Error Level", "Error Reason", "Additional Info");
        Mono<Void> result = globalExceptionHandler.handle(exchange, badRequestException);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleInternalException() {
        InternalException internalException = new InternalException("Error Code", "Error Level", "Error Reason");
        Mono<Void> result = globalExceptionHandler.handle(exchange, internalException);
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void handleTechnicalException() {
        TechnicalException technicalException = new TechnicalException("Error Code", "Error Level", "Error Reason");
        Mono<Void> result = globalExceptionHandler.handle(exchange, technicalException);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void handleControllersException() {
        ControllersException controllersException = new ControllersException("Error Code", "Error Level", "Error Reason");
        Mono<Void> result = globalExceptionHandler.handle(exchange, controllersException);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleExpiredJwtException() {
        ExpiredJwtException expiredJwtException = new ExpiredJwtException(null, null, "Token expired");
        Mono<Void> result = globalExceptionHandler.handle(exchange, expiredJwtException);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
