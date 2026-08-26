package com.yuhan.fleetflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fleetFlowOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("FleetFlow API")
                                .version("1.0")
                                .description(
                                        "REST API for freight quotation, payment, job scheduling, and fleet operations."
                                )
                );
    }
}