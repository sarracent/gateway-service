package com.claro.amx.sp.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.claro.amx"})
@SpringBootApplication
public class SpApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpApiGatewayApplication.class, args);
	}

}
