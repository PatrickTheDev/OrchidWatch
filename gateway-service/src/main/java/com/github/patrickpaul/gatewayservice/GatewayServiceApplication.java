package com.github.patrickpaul.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("scraping-service", r -> r
                        .path("/scraping/health")
                        .filters(f -> f
                                .rewritePath("/scraping/health", "/actuator/health"))
                        .uri("http://localhost:8762"))
                .route("orchids-service", r -> r
                        .path("/orchids/health")
                        .filters(f -> f
                                .rewritePath("/orchids/health", "/actuator/health"))
                        .uri("http://localhost:8763"))
                .build();
    }

}
