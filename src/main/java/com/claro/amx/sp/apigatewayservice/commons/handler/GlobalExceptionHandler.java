package com.claro.amx.sp.apigatewayservice.commons.handler;


import com.claro.amx.sp.apigatewayservice.commons.aop.LogAspect;
import com.claro.amx.sp.apigatewayservice.commons.resolver.CustomExceptionResolverDelegate;
import com.claro.amx.sp.apigatewayservice.exception.impl.*;
import com.claro.amx.sp.apigatewayservice.model.response.ServiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @ExceptionHandler({InternalException.class, ControllersException.class, BadRequestException.class,
            ConstraintViolationException.class, BusinessException.class, TechnicalException.class,
            ExpiredJwtException.class, Exception.class})
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        if (exception instanceof BusinessException || exception instanceof TechnicalException ||
                exception instanceof ExpiredJwtException) {
            try {
                return respondOtherExceptions(exchange, serviceResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (exception instanceof ControllersException || exception instanceof BadRequestException ||
                exception instanceof ConstraintViolationException) {
            try {
                return respondBadRequestExceptions(exchange, serviceResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (exception instanceof InternalException) {
            try {
                return respondInternalExceptions(exchange, serviceResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Mono.error(exception);
    }


    private Mono<Void> respondOtherExceptions(ServerWebExchange exchange, ServiceResponse response) throws JsonProcessingException {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(response));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private Mono<Void> respondBadRequestExceptions(ServerWebExchange exchange, ServiceResponse response) throws JsonProcessingException {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(response));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private Mono<Void> respondInternalExceptions(ServerWebExchange exchange, ServiceResponse response) throws JsonProcessingException {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(response));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}