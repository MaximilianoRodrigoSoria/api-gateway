package com.ar.laboratory.apigateway.gateway.domain.model;

/**
 * Definición de ruta del gateway: los requests cuyo path (tras {@code /gateway}) empieza con {@code
 * pathPrefix} se reenvían a {@code targetUri}.
 */
public record RouteDefinition(String id, String pathPrefix, String targetUri) {

    public boolean matches(String path) {
        return path != null && path.startsWith(pathPrefix);
    }
}
