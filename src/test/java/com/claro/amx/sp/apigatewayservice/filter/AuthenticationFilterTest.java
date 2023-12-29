package com.claro.amx.sp.apigatewayservice.filter;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.*;
import static com.claro.amx.sp.apigatewayservice.constants.Errors.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.claro.amx.sp.apigatewayservice.exception.impl.BadRequestException;
import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private Predicate<ServerHttpRequest> isSecured;

    @Mock
    private JwtService jwtService;

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(authenticationFilter, "originPath", "https://psp.claro.com.ar");
    }

    @Test
    void shouldThrowBadRequestExceptionWhenAuthorizationHeaderMissing() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        when(isSecured.test(any())).thenReturn(true);
        when(headers.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationFilter.filter(exchange, chain);
        });

        assertEquals(ERROR_BADREQUEST_FAILHEADER_REQUIRED.getCode(), exception.getCode());
        assertEquals(ERROR_BADREQUEST_FAILHEADER_REQUIRED.getLevel(), exception.getLevel());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenOriginIsNotValid() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        when(isSecured.test(any())).thenReturn(true);
        when(headers.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationFilter.filter(exchange, chain);
        });

        assertEquals(ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getCode(), exception.getCode());
        assertEquals(ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getLevel(), exception.getLevel());
    }

    @Test
    void shouldThrowBusinessExceptionWhenTokenExpired() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        List<String> authorizationHeaderList = Collections.singletonList("Bearer eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiU2Vzc2lvbi1JZCI6IkRQUiIsIkNoYW5uZWwtSWQiOiJQU1AtNiIsImlhdCI6MTY5NTc3NjI0MiwiZXhwIjoxNjk1Nzc4MDQyfQ.eouU3JmVMeogOhSLWaTfO4cd6deo6nWZ-gNDo_4wSkM");
        List<String> originHeaderList = Collections.singletonList("https://psp.claro.com.ar");

        when(headers.get(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeaderList);
        when(headers.get(HttpHeaders.ORIGIN)).thenReturn(originHeaderList);
        when(isSecured.test(any())).thenReturn(true);
        when(headers.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(true);
        when(jwtService.validateToken(any())).thenThrow(MalformedJwtException.class);


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authenticationFilter.filter(exchange, chain);
        });

        assertEquals(ERROR_TOKEN_INVALID.getCode(), exception.getCode());
        assertEquals(ERROR_TOKEN_INVALID.getLevel(), exception.getLevel());
    }

    @Test
    void testFilter() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        List<String> authorizationHeaderList = Collections.singletonList("Bearer eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiU2Vzc2lvbi1JZCI6IkRQUiIsIkNoYW5uZWwtSWQiOiJQU1AtNiIsImlhdCI6MTY5NTc3NjI0MiwiZXhwIjoxNjk1Nzc4MDQyfQ.eouU3JmVMeogOhSLWaTfO4cd6deo6nWZ-gNDo_4wSkM");
        List<String> originHeaderList = Collections.singletonList("https://psp.claro.com.ar");

        when(headers.get(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeaderList);
        when(headers.get(HttpHeaders.ORIGIN)).thenReturn(originHeaderList);
        when(isSecured.test(any())).thenReturn(true);
        when(headers.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(true);

        when(jwtService.validateToken(any())).thenReturn(true);
        Claims mockClaims = Mockito.mock(Claims.class);
        when(jwtService.extractAllClaims(anyString())).thenReturn(mockClaims);
        when(mockClaims.get(CHANNEL_NAME, String.class)).thenReturn("PSP");
        when(mockClaims.get(USER_NAME, String.class)).thenReturn("EXA743655");
        when(mockClaims.get(SESSION_NAME, String.class)).thenReturn("DPR");

        ServerHttpRequest.Builder requestBuilder = Mockito.mock(ServerHttpRequest.Builder.class);
        when(request.mutate()).thenReturn(requestBuilder);

        when(requestBuilder.header(CHANNEL_NAME, "PSP")).thenReturn(requestBuilder);
        when(requestBuilder.header(USER_NAME, "EXA743655")).thenReturn(requestBuilder);
        when(requestBuilder.header(SESSION_NAME, "DPR")).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(request);

        ServerWebExchange.Builder exchangeBuilder = Mockito.mock(ServerWebExchange.Builder.class);
        when(exchange.mutate()).thenReturn(exchangeBuilder);
        when(exchangeBuilder.request(any(ServerHttpRequest.class))).thenReturn(exchangeBuilder);
        when(exchangeBuilder.build()).thenReturn(exchange);
        when(chain.filter(any())).thenReturn(Mono.empty());

        authenticationFilter.filter(exchange, chain);

        verify(chain, times(1)).filter(any());
    }


}
