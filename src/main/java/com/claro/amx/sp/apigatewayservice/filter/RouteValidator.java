package com.claro.amx.sp.apigatewayservice.filter;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@AllArgsConstructor
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of("/auth/token");

    @Bean
    public Predicate<ServerHttpRequest> isSecured() {
        return request -> openApiEndpoints
                .stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }

}
