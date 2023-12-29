package com.claro.amx.sp.apigatewayservice.filter;

import com.claro.amx.sp.apigatewayservice.constants.FilterOrder;
import com.claro.amx.sp.apigatewayservice.exception.impl.BadRequestException;
import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.*;
import static com.claro.amx.sp.apigatewayservice.constants.Errors.*;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${origin.path}")
    private String originPath;

    private final Predicate<ServerHttpRequest> isSecured;

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = null;
        if (isSecured.test(exchange.getRequest())) {
            //header contains token and origin or not
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new BadRequestException(
                        String.format(ERROR_BADREQUEST_FAILHEADER_REQUIRED.getReason(),HttpHeaders.AUTHORIZATION),
                        ERROR_BADREQUEST_FAILHEADER_REQUIRED.getCode(),
                        ERROR_BADREQUEST_FAILHEADER_REQUIRED.getLevel(),
                        ERROR_BADREQUEST_FAILHEADER_REQUIRED.getMessage());
            }

            String authorizationHeader = Optional.ofNullable(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION))
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElse(null);
            String originHeader =  Optional.ofNullable(exchange.getRequest().getHeaders().get(HttpHeaders.ORIGIN))
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElse(null);

            if(!isValidOrigin(originHeader)) {
                throw new BadRequestException(
                        String.format(ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getReason(),originHeader),
                        ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getCode(),
                        ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getLevel(),
                        ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN.getMessage());
            }

            if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
                authorizationHeader = authorizationHeader.substring(7);
            }

            try {
                jwtService.validateToken(String.valueOf(authorizationHeader));
                request = exchange.getRequest()
                        .mutate()
                        .header(CHANNEL_NAME, jwtService.extractAllClaims(authorizationHeader)
                                .get(CHANNEL_NAME, String.class))
                        .header(USER_NAME, jwtService.extractAllClaims(authorizationHeader)
                                .get(USER_NAME, String.class))
                        .header(SESSION_NAME, jwtService.extractAllClaims(authorizationHeader)
                                .get(SESSION_NAME, String.class))
                        .build();

            } catch (JwtException e) {
                throw new BusinessException(
                        String.format(ERROR_TOKEN_INVALID.getReason(), authorizationHeader),
                        ERROR_TOKEN_INVALID.getCode(),
                        ERROR_TOKEN_INVALID.getLevel(),
                        ERROR_TOKEN_INVALID.getMessage());
            }
        }
        return chain.filter(exchange.mutate().request(request).build())
                .then(Mono.defer(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders responseHeaders = response.getHeaders();

                    List<String> headers = responseHeaders.get("Access-Control-Allow-Origin");

                    if (headers != null) {
                        headers = headers.stream().filter(h -> !h.equals("*")).collect(Collectors.toList());

                        responseHeaders.put("Access-Control-Allow-Origin", headers);
                    }

                    return Mono.empty();
                }));
    }


    private boolean isValidOrigin(String originHeader) {
        return Objects.nonNull(originHeader) &&
                (originHeader.equals(originPath) || originHeader.equals("http://localhost:4000"));
    }

    @Override
    public int getOrder() {
        return FilterOrder.AUTHENTICATION_FILTER_ORDER;
    }
}
