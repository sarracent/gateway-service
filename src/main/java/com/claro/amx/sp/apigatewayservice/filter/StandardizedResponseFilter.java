package com.claro.amx.sp.apigatewayservice.filter;

import com.claro.amx.sp.apigatewayservice.business.ServiceMessagesBO;
import com.claro.amx.sp.apigatewayservice.constants.FilterOrder;
import com.claro.amx.sp.apigatewayservice.model.response.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class StandardizedResponseFilter implements GlobalFilter, Ordered {

    private final ServiceMessagesBO serviceMessagesBO;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().response(new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                ServerHttpRequest request = exchange.getRequest();
                String path = request.getURI().getPath();

                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // Here you can modify the data in the buffers
                        // For example, concatenate and convert to a string
                        String originalBody = dataBuffers.stream()
                                .map(dataBuffer -> {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);
                                    return new String(content, StandardCharsets.UTF_8);
                                }).collect(Collectors.joining());

                        // Manipulate the body as needed
                        String modifiedBody = modifyBody(originalBody, path);

                        // Then write the new data buffer
                        byte[] newBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
                        return new DefaultDataBufferFactory().wrap(newBytes);
                    }));
                }
                // if body is not a Flux<DataBuffer> just pass through
                return super.writeWith(body);
            }
        }).build());
    }

    private String modifyBody(String originalBody, String path) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode originalRoot = objectMapper.readTree(originalBody);

            JsonNode workingObject = originalRoot.has("serviceDetails") ? originalRoot.get("serviceDetails") : originalRoot;

            String code = findValue(workingObject, new String[]{"code", "result", "error", "resultCode"});
            String reason = findValue(workingObject, new String[]{"reason", "error", "result", "resultMessage", "message"});

            if (!isNum(code)) {
                String temp = code;
                code = reason;
                reason = temp;
            }
            final var serviceMessages = serviceMessagesBO.getServiceMessages(path, code);

            String level = serviceMessages.getLevel();
            String service = serviceMessages.getService();
            String message = serviceMessages.getMessage();

            UserResponse userResponse = UserResponse.builder()
                    .reason(reason)
                    .code(code)
                    .level(level)
                    .service(service)
                    .message(message)
                    .build();

            ObjectNode newRoot = objectMapper.createObjectNode();
            newRoot.set("userResponse", objectMapper.valueToTree(userResponse));
            newRoot.setAll((ObjectNode) originalRoot);

            return objectMapper.writeValueAsString(newRoot);
        } catch (Exception e) {
            return originalBody;
        }
    }

    private String findValue(JsonNode obj, String[] fields) {
        for (String field : fields) {
            if (obj.has(field)) {
                return obj.get(field).asText(); // Converts the value to String
            }
        }
        return null;
    }

    private static boolean isNum(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public int getOrder() {
        return FilterOrder.STANDARDIZE_RESPONSE_FILTER_ORDER;
    }
}



