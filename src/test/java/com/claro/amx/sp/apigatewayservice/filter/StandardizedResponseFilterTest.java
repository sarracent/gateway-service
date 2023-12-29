package com.claro.amx.sp.apigatewayservice.filter;

import com.claro.amx.sp.apigatewayservice.business.ServiceMessagesBO;
import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardizedResponseFilterTest {

    @Mock
    private ServiceMessagesBO serviceMessagesBO;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private StandardizedResponseFilter filter;

    private ServerWebExchange exchange;
    private ServerHttpResponse response;

    @BeforeEach
    void setUp() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/test").build();
        exchange = MockServerWebExchange.from(request);
        response = exchange.getResponse();
    }

    @Test
    void filterTest_SuccessfulResponse() {
        ServiceMessages serviceMessages = ServiceMessages.builder()
                .level("INFO")
                .service("Test Service")
                .message("Test Message")
                .build();
        when(serviceMessagesBO.getServiceMessages(anyString(), anyString())).thenReturn(serviceMessages);


        String responseBody = "{\"code\":\"200\",\"message\":\"Success\"}";
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        when(filterChain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange exchange = invocation.getArgument(0);
            return exchange.getResponse().writeWith(Flux.just(dataBuffer));
        });

        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        verify(serviceMessagesBO, times(1)).getServiceMessages(anyString(), anyString());
    }


}
