package com.semiramide.timetracker.gateway.config;

import com.semiramide.timetracker.gateway.properties.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, GatewayProperties props) {
        return builder
                .routes()
                .route("backend_service",
                        p ->
                                p.path(
                                                "/project/**",
                                                "/employee/**",
                                                "/worklog/**",
                                                "/leave/**",
                                                "/project-employees/**",
                                                "/export/**",
                                                "/test/**")
                .filters(f -> f.prefixPath("/backend_service"))
                .uri("http://" + props.getBackendHost() + "/"))
                .build();
    }
}
