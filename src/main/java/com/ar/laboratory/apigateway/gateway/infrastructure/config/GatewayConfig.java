package com.ar.laboratory.apigateway.gateway.infrastructure.config;

import com.ar.laboratory.apigateway.gateway.application.inbound.command.ForwardRequestCommand;
import com.ar.laboratory.apigateway.gateway.application.outbound.port.DownstreamClientPort;
import com.ar.laboratory.apigateway.gateway.application.service.RouteRegistry;
import com.ar.laboratory.apigateway.gateway.application.usecase.ForwardRequestUseCase;
import com.ar.laboratory.apigateway.gateway.domain.model.RouteDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wiring del gateway. */
@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayConfig {

    @Bean
    public RouteRegistry routeRegistry(GatewayProperties properties) {
        return new RouteRegistry(
                properties.getRoutes().stream()
                        .map(
                                r ->
                                        new RouteDefinition(
                                                r.getId(), r.getPathPrefix(), r.getTargetUri()))
                        .toList());
    }

    @Bean
    public ForwardRequestCommand forwardRequestCommand(
            RouteRegistry routeRegistry, DownstreamClientPort downstream) {
        return new ForwardRequestUseCase(routeRegistry, downstream);
    }
}
