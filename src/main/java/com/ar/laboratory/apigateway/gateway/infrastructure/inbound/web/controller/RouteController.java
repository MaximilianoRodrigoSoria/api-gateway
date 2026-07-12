package com.ar.laboratory.apigateway.gateway.infrastructure.inbound.web.controller;

import com.ar.laboratory.apigateway.gateway.application.service.RouteRegistry;
import com.ar.laboratory.apigateway.gateway.infrastructure.inbound.web.dto.RouteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Consulta de las rutas configuradas en el gateway. */
@Tag(name = "Routes", description = "Rutas del gateway")
@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteRegistry routeRegistry;

    @GetMapping
    public ResponseEntity<List<RouteResponse>> list() {
        List<RouteResponse> routes =
                routeRegistry.all().stream()
                        .map(r -> new RouteResponse(r.id(), r.pathPrefix(), r.targetUri()))
                        .toList();
        return ResponseEntity.ok(routes);
    }
}
