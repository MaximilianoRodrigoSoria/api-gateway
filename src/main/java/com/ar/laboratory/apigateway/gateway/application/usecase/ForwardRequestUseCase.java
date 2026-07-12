package com.ar.laboratory.apigateway.gateway.application.usecase;

import com.ar.laboratory.apigateway.gateway.application.inbound.command.ForwardRequestCommand;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayRequest;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;
import com.ar.laboratory.apigateway.gateway.application.outbound.port.DownstreamClientPort;
import com.ar.laboratory.apigateway.gateway.application.service.RouteRegistry;
import com.ar.laboratory.apigateway.gateway.domain.exception.RouteNotFoundException;
import com.ar.laboratory.apigateway.gateway.domain.model.RouteDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resuelve la ruta para el request entrante, construye la URL del downstream y reenvía la llamada,
 * devolviendo su respuesta tal cual. POJO sin framework.
 */
@Slf4j
@RequiredArgsConstructor
public class ForwardRequestUseCase implements ForwardRequestCommand {

    private final RouteRegistry routes;
    private final DownstreamClientPort downstream;

    @Override
    public GatewayResponse execute(GatewayRequest request) {
        RouteDefinition route =
                routes.resolve(request.path())
                        .orElseThrow(() -> new RouteNotFoundException(request.path()));
        String url = route.targetUri() + request.path();
        if (request.query() != null && !request.query().isBlank()) {
            url = url + "?" + request.query();
        }
        log.debug("Reenviando {} {} -> {}", request.method(), request.path(), url);
        return downstream.forward(
                request.method(), url, request.headers(), request.body());
    }
}
