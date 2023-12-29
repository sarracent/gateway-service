package com.claro.amx.sp.apigatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiGatewayConfiguration {

    @Value("${generate.token.uri}")
    private String generateTokenUri;
    @Value("${generate.token.path}")
    private String generateTokenPath;

    @Value("${data.consumption.uri}")
    private String dataConsumptionUri;
    @Value("${data.consumption.filter.list.path}")
    private String dataConsumptionFilterListPath;
    @Value("${data.consumption.path}")
    private String dataConsumptionPath;

    @Value("${line.data.uri}")
    private String lineDataUri;
    @Value("${line.data.path}")
    private String lineDataPath;

    @Value("${line.check.uri}")
    private String lineCheckUri;
    @Value("${line.check.path}")
    private String lineCheckPath;



    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path(generateTokenPath)
                        .uri(generateTokenUri))
                .route(r -> r.path(dataConsumptionFilterListPath)
                        .uri(dataConsumptionUri))
                .route(r -> r.path(dataConsumptionPath)
                        .uri(dataConsumptionUri))
                .route(r -> r.path(lineDataPath)
                        .uri(lineDataUri))
                .route(r -> r.path(lineCheckPath)
                        .uri(lineCheckUri))
                .build();
    }
}
