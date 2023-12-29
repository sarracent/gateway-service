package com.claro.amx.sp.apigatewayservice.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URI;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteValidatorTest {

    private RouteValidator routeValidator;

    @BeforeEach
    void setUp() {
        routeValidator = new RouteValidator();
    }

    @Test
    void isSecured_openApiEndpoints_ReturnsFalse() {
        // Crear un mock de ServerHttpRequest
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        URI mockUri = URI.create("/auth/token");
        when(request.getURI()).thenReturn(mockUri);

        Predicate<ServerHttpRequest> predicate = routeValidator.isSecured();

        assertFalse(predicate.test(request));
    }

    @Test
    void isSecured_otherEndpoints_ReturnsTrue() {
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        URI mockUri = URI.create("/another/endpoint");
        when(request.getURI()).thenReturn(mockUri);

        Predicate<ServerHttpRequest> predicate = routeValidator.isSecured();

        assertTrue(predicate.test(request));
    }

}