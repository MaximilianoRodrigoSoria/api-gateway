package com.ar.laboratory.apigateway.gateway.application.service;

import com.ar.laboratory.apigateway.gateway.domain.model.RouteDefinition;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Registro de rutas: resuelve una ruta por el prefijo más largo que matchee el path. */
public class RouteRegistry {

    private final List<RouteDefinition> routes;

    public RouteRegistry(List<RouteDefinition> routes) {
        this.routes = routes == null ? List.of() : routes;
    }

    public Optional<RouteDefinition> resolve(String path) {
        return routes.stream()
                .filter(r -> r.matches(path))
                .max(Comparator.comparingInt(r -> r.pathPrefix().length()));
    }

    public List<RouteDefinition> all() {
        return List.copyOf(routes);
    }
}
